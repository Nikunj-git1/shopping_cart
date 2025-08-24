package com.example.shopping_cart.security;

import com.example.shopping_cart.service_impl.UserLoginServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    // JwtAuthFilter एक कस्टम फिल्टर क्लास है जो JWT टोकन को सत्यापित करती है
    // JwtAuthFilter is a custom filter class that verifies JWT tokens
    private final UserLoginServiceImpl userLoginServiceImpl;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(UserLoginServiceImpl userLoginServiceImpl, ObjectMapper objectMapper) {
        log.info("-----JwtAuthFilter {} {} ", userLoginServiceImpl, objectMapper);
        this.userLoginServiceImpl = userLoginServiceImpl;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("-----doFilterInternal {} {} {} ", request, response, filterChain);
        String path = request.getRequestURI();

// To bypass Swagger URLs
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

// The JWT token is extracted from the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

// If the header starts with "Bearer ", extract the token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            username = JwtHelper.extractUsername(token);
        }
        if (token == null) {
// If the token is null, pass the request to the next filter
            filterChain.doFilter(request, response);
            return; // Skip the rest of the process if token is missing
        }

// यदि यूज़रनेम उपलब्ध है और सिक्योरिटी कॉन्टेक्स्ट में कोई ऑथेंटिकेशन नहीं है
// If username is available and no authentication is present in the security context
        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userLoginServiceImpl.loadUserByUsername(username);
// User details are retrieved
                if (JwtHelper.validateToken(token, userDetails)) {
// If the token is valid
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, null);
// एक नया ऑथेंटिकेशन टोकन तैयार किया जाता है
// A new authentication token is created
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// Set the request details into the authentication object
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
// Set authentication in the security context
                }
            }
            filterChain.doFilter(request, response);
// Pass the request to the next filter in the chain
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
// Set Forbidden (403) status code on any exception
            response.getWriter().write("Access denied");
// Send "Access denied" message to the client
        }
    }
}