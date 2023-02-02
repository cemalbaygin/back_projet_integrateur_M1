package fr.uga.miage.m1.auth;

import fr.uga.miage.m1.constants.Role;
import fr.uga.miage.m1.entity.Utilisateur;
import fr.uga.miage.m1.model.mapper.AutoMapper;
import fr.uga.miage.m1.repository.UserRepository;
import fr.uga.miage.m1.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final AutoMapper autoMapper = Mappers.getMapper(AutoMapper.class);

    public AuthenticationResponse register(RegisterRequest request) {
        Utilisateur u = userRepository.findByEmail(request.getEmail().toLowerCase()).orElse(null);

        if (u == null) {
            u = Utilisateur.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(u);
        }

        var jwtToken = jwtService.generateToken(u);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .utilisateur(autoMapper.entityToDto(u))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        Utilisateur user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .utilisateur(autoMapper.entityToDto(user))
                .build();
    }
}
