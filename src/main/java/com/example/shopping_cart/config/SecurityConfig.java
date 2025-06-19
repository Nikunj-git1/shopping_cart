package com.example.shopping_cart.config;

import com.example.shopping_cart.security.JwtAuthFilter;
import com.example.shopping_cart.service_impl.UserLoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Slf4j
@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserLoginServiceImpl userLoginServiceImpl;


    public SecurityConfig(UserLoginServiceImpl userLoginServiceImpl, JwtAuthFilter jwtAuthFilter) {
        log.info("userServiceImpl {}------");

        this.userLoginServiceImpl = userLoginServiceImpl;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationManager authenticationManager) throws Exception {
        log.info("filterChain {}------");

        return http
                .cors(cors -> cors.configurationSource(
                        request -> new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/admin/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/customer/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/customer/login").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
//                                "/redoc.html", "/openapi.yaml"
                        ).permitAll()
                        .requestMatchers("/ui/**").permitAll()
                        .requestMatchers("/factory-pattern/**").permitAll()


                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("passwordEncoder {}------");


        return new BCryptPasswordEncoder();

//    For Plain Pass. Login
//    return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("authenticationManager {}------");

        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userLoginServiceImpl).passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}