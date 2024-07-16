package com.shopme.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig  {


    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http.authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/users/**","/settings/**","/countries/**","/states/**").hasAuthority("Admin")
                                .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
                                .requestMatchers("/products/new", "/products/delete/**").hasAnyAuthority("Admin", "Editor")
                                .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique").hasAnyAuthority("Admin", "Editor", "Salesperson")
                                .requestMatchers("/products", "/products/", "/products/detail/**", "/products/page/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                                .requestMatchers("/products/**").hasAnyAuthority("Admin", "Editor")
                                .anyRequest().authenticated()
                )
                .formLogin((form)-> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .permitAll())
                        .logout(logout-> logout.permitAll())
                        .rememberMe(rem-> rem
                                .key("Abcdefghijklmnop_1234567890")
                                .tokenValiditySeconds(7 * 24 * 60 * 60));

                        //.requestMatchers("/","/ShopmeAdmin").permitAll().anyRequest().authenticated();

        return http.build();

    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
                .requestMatchers("/images/**", "/js/**","/webjars/**");

    }


    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permiteAll();
    }


     */
}
