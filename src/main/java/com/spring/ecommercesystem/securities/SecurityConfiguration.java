package com.spring.ecommercesystem.securities;

import com.spring.ecommercesystem.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService service){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(service);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //
    @Bean
    public CsrfTokenRepository csrfTokenRepository(){
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setParameterName("_csrf");
        return repository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //Configure the CSRF to ignore APIs
        httpSecurity.csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/**")
        );

        //Configure Security
        httpSecurity.authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/icons/**", "/src/main/resources/static/userAvatar/**").permitAll()
//                        .requestMatchers("/home/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .anyRequest().authenticated()
        ).formLogin(
                form -> form.loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
        ).logout(
                logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/home")
                        .permitAll()
        ).exceptionHandling(
                configurer -> configurer
                        .accessDeniedPage("/login/403")
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/login"))// handle requiring authenticate when the user access


        );


        return httpSecurity.build();
    }

}