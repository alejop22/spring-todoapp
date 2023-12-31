package com.alejopp.todoapp.context.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alejopp.todoapp.context.security.filter.JwtAuthenticationFilter;
import com.alejopp.todoapp.context.security.filter.JwtAuthorizationFilter;
import com.alejopp.todoapp.context.security.jwt.JwtUtils;
import com.alejopp.todoapp.service.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthorizationFilter authorizationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsServiceImpl;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(this.jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/users").permitAll();
                auth.anyRequest().authenticated();
            })
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    PasswordEncoder passwordEncoder() { // Para encriptar la contraseña
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception { // Se encarga de administrar la autenticacion
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    // Configuracion sin JWT
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    //     return httpSecurity
    //         // .csrf(csrf -> csrf.disable()) // Esta configuracion evita la interceptacion de los datos enviados a nuestros endpoints
    //         .authorizeHttpRequests((authorizeHttpRequests) -> {
    //             authorizeHttpRequests
    //                 .requestMatchers("/v1/tasks") // Puedo acceder a este endpoint sin autenticacion
    //                 .permitAll()
    //                 .anyRequest() 
    //                 .authenticated(); // Para cualquier otro endpoint debo poner mi usuario y contraseña
    //         })
    //         .formLogin((formLogin) -> {
    //             formLogin
    //                 .successHandler(successHandler()) // Cuando se haga login redirige al endpoint configurado
    //                 .permitAll(); // Para que se muestre el formulario de login a todos
    //         })
    //         .sessionManagement((sessionManagement) -> {
    //             sessionManagement
    //                 .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Crea y mantiene una sesion
    //                 .invalidSessionUrl("/login") // Cuando el usuario no tiene sesion lo manda al /login
    //                 .maximumSessions(1) // Maximo de sesiones por usuario
    //                 .expiredUrl("/login") // Cuando se expire la sesion del usuario se redirige al /login
    //                 .sessionRegistry(sessionRegistry()); // Nos permite ver los datos de la sesion del usuario que se autentique
    //         })
    //         .httpBasic((httpBasic) -> {}) // withDefaults. Esto para activar el Basic Auth en postman
    //         .build();
    // }

    // private AuthenticationSuccessHandler successHandler() {
    //     return ((request, response, authentication) -> {
    //         response.sendRedirect("/v1/tasks");
    //     });
    // }
}
