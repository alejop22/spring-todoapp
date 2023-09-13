package com.alejopp.todoapp.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            // .csrf(csrf -> csrf.disable()) // Esta configuracion evita la interceptacion de los datos enviados a nuestros endpoints
            .authorizeHttpRequests((authorizeHttpRequests) -> {
                authorizeHttpRequests
                    .requestMatchers("/v1/tasks") // Puedo acceder a este endpoint sin autenticacion
                    .permitAll()
                    .anyRequest() 
                    .authenticated(); // Para cualquier otro endpoint debo poner mi usuario y contraseña
            })
            .formLogin((formLogin) -> {
                formLogin
                    .successHandler(successHandler()) // Cuando se haga login redirige al endpoint configurado
                    .permitAll(); // Para que se muestre el formulario de login a todos
            })
            .sessionManagement((sessionManagement) -> {
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Crea y mantiene una sesion
                    .invalidSessionUrl("/login") // Cuando el usuario no tiene sesion lo manda al /login
                    .maximumSessions(1) // Maximo de sesiones por usuario
                    .expiredUrl("/login") // Cuando se expire la sesion del usuario se redirige al /login
                    .sessionRegistry(sessionRegistry()); // Nos permite ver los datos de la sesion del usuario que se autentique
            })
            .httpBasic((httpBasic) -> {}) // withDefaults. Esto para activar el Basic Auth en postman
            .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    private AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/tasks");
        });
    }
}
