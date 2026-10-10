package com.FindAJob.demo.SecurityPackage;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

//password hasher
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

//initialization of filter with jwt service and userRepo
    @Bean
    public AuthFilter authFilter(JWTService jwtSvc, CustomUserDetailsService user) {
        return new AuthFilter(jwtSvc, user);
    }

//
    @Bean
    public FilterRegistrationBean<AuthFilter> authFilterRegistration(AuthFilter filter) {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);//this way springboot doesn't attempt to automatically initialize it creating a conflict
        return registrationBean;
    }


//Provider has no access to database, instead it uses references to
// direct requests to other functions that can communicate with the DB

    @Bean
    public AuthenticationProvider authProvider(CustomUserDetailsService userDetServ,
                                               PasswordEncoder encoder){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetServ);
        provider.setPasswordEncoder(encoder);

        return provider;
    }
//manages the different filters if we have more than one
    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthFilter authFilter
                                                   ) throws Exception {
    http

        .addFilterBefore(
            authFilter,
            UsernamePasswordAuthenticationFilter.class
        );

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        //Permitted endpoints for all users
                        .requestMatchers("/app/users/create",
                                "/app/users/logIn",
                                "/app/comp/create",
                                "/app/comp/logIn",
                                "/error",
                                "/app/refresh/userrefAcc",
                                "/app/refresh/comprefAcc",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()

                        //Authorized endpoints for reg_users
                        .requestMatchers("/app/users/update/{id}",
                                "/app/users/delete/{id}",
                                "/app/jobs/all",
                                "/app/application/view_applications",
                                "/app/application/apply").hasRole("Reg_User")

                        //Authorized endpoints for companies
                        .requestMatchers("/app/jobs/create",
                                "/app/jobs/update/{id}",
                                "/app/jobs/delete/{id}",
                                "/app/application/comp_view_app",
                                "/app/jobs/jobsbycompany",
                                "/app/application/company_assesses/{id}").hasRole("Company")


                        .requestMatchers("/app/jobs/one/{id}",
                                "/app/jobs/all")
                        .hasAnyRole("Reg_User", "Company")

                        .requestMatchers("/app/users/delete/{id}",
                                "/app/application/Adminget",
                                "/app/refresh/deleteRefTokens",
                                "/app/comp/delete/{id}").hasRole("Admin")


                );


        return http.build();
    }
}
