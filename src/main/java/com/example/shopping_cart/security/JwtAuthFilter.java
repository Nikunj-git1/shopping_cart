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
    JwtHelper jwtHelper;

    // JwtAuthFilter एक कस्टम फिल्टर क्लास है जो JWT टोकन को सत्यापित करती है
    private final UserLoginServiceImpl userLoginServiceImpl;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(UserLoginServiceImpl userLoginServiceImpl, ObjectMapper objectMapper) {
        this.userLoginServiceImpl = userLoginServiceImpl;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

// यह ओवरराइड मेथड हर HTTP अनुरोध पर कॉल होता है
        String path = request.getRequestURI();

// Swagger URLs को बायपास किया जाता है
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }

// Authorization हेडर से JWT टोकन को प्राप्त किया जाता है
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

// यदि हेडर "Bearer " से शुरू होता है तो टोकन को अलग करें
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = JwtHelper.extractUsername(token);
        }
        if (token == null) {

// यदि टोकन null है, तो यह अनुरोध को अगले फिल्टर में पास कर देगा
            filterChain.doFilter(request, response);
            return; // टोकन नहीं होने पर बाकी प्रोसेस को स्किप कर देता है
        }

// यदि यूज़रनेम उपलब्ध है और सिक्योरिटी कॉन्टेक्स्ट में कोई ऑथेंटिकेशन नहीं है)


        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userLoginServiceImpl.loadUserByUsername(username);
// यूज़र की डिटेल्स प्राप्त की जाती हैं
                if (JwtHelper.validateToken(token, userDetails)) {
// यदि टोकन वैध है तो
                    logger.info("if (JwtHelper.validateToken---------");

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, null);
// एक नया ऑथेंटिकेशन टोकन तैयार किया जाता है

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// अनुरोध की डिटेल्स ऑथेंटिकेशन में सेट की जाती हैं

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
// सिक्योरिटी कॉन्टेक्स्ट में ऑथेंटिकेशन सेट किया जाता है
                }
            }
            filterChain.doFilter(request, response);
// अनुरोध को अगली फिल्टर चेन में पास किया जाता है
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
// कोई भी अपवाद आने पर Forbidden (403) स्टेटस कोड सेट किया जाता है

            response.getWriter().write("Access denied");
// और क्लाइंट को "Access denied" संदेश भेजा जाता है
        }
    }
}