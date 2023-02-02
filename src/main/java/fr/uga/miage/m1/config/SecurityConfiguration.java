package fr.uga.miage.m1.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableScheduling
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private final HttpRequestEndpointChecker endpointChecker;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors().disable()
                .authorizeHttpRequests()
                .requestMatchers("/public/**")
                .permitAll()
                .and().authorizeHttpRequests()
                .requestMatchers("/private/**")
                .authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new MyAuthenticationEntryPoint(endpointChecker))
                .accessDeniedHandler(new MyAccessDeniedHandler(endpointChecker));

        return http.build();
    }

    public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {
        private HttpRequestEndpointChecker endpointChecker;

        public MyAccessDeniedHandler(HttpRequestEndpointChecker endpointChecker) {
            this.endpointChecker = endpointChecker;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException, ServletException {

            if (!endpointChecker.isEndpointExist(request)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
            } else {
                super.handle(request, response, accessDeniedException);
            }
        }
    }

    public class MyAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

        private HttpRequestEndpointChecker endpointChecker;

        public MyAuthenticationEntryPoint(HttpRequestEndpointChecker endpointChecker) {
            this.endpointChecker = endpointChecker;
        }

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                             AuthenticationException authException) throws IOException {
            if (!endpointChecker.isEndpointExist(request)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
            } else {
                super.commence(request, response, authException);
            }
        }
    }
}
