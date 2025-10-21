/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.RodriguezPetVet.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configura las rutas accesibles sin iniciar sesión
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/dashboard/**").hasRole("ADMIN") // solo admin
                .anyRequest().authenticated() // cualquier otra ruta requiere login
            )

            // Configura el formulario de login
            .formLogin(form -> form
                .loginPage("/login")           // tu página login.html
                .defaultSuccessUrl("/", true) 
                .permitAll()
            )

            // Configura el logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // Página personalizada si el usuario no tiene permisos
            .exceptionHandling(ex -> ex.accessDeniedPage("/acceso_denegado"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}










//     @Bean
//     public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
//         http
//             .csrf().disable() //
//             .authorizeHttpRequests(auth -> auth
//                 .anyRequest().permitAll() // 
//             )
//             .formLogin().disable() // 
//             .logout().disable();    // 

//         return http.build();
//     }
// }


