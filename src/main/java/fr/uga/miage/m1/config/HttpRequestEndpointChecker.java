package fr.uga.miage.m1.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class HttpRequestEndpointChecker {

    private DispatcherServlet servlet;

    public HttpRequestEndpointChecker(DispatcherServlet servlet) {
        this.servlet = servlet;
    }

    public boolean isEndpointExist(HttpServletRequest request) {

        for (HandlerMapping handlerMapping : servlet.getHandlerMappings()) {
            try {
                HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                if (foundHandler != null) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}