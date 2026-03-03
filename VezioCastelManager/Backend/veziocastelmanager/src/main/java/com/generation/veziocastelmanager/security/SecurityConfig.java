package com.generation.veziocastelmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // inietto il filtro JWT che controlla il token su ogni richiesta
    @Autowired
    private JwtAuthenticationFilter         jwtAuthFilter;

    @Bean
    public SecurityFilterChain           securityFilterChain(HttpSecurity http) throws Exception
    {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // abilito CORS con la configurazione definita sotto
            .csrf(csrf -> csrf.disable())   // disabilito il csrf perché con le API REST non serve
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // le preflight OPTIONS devono passare senza token, altrimenti il CORS non funziona
                .requestMatchers("/vcm/api/users/login").permitAll()  // solo il login è pubblico
                .anyRequest().authenticated()                         // tutto il resto richiede autenticazione
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // aggiungo il mio filtro JWT prima di quello di default di Spring
        return http.build();
    }

    // definisco la configurazione CORS: quali origini, metodi e header sono permessi
    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));                             // solo il frontend Angular
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));          // tutti i metodi HTTP che usiamo
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));                    // header necessari per JWT e JSON
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);    // applico la configurazione a tutti gli endpoint
        return source;
    }
}
