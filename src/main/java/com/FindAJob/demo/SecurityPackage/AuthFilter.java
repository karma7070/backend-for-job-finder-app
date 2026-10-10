package com.FindAJob.demo.SecurityPackage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JWTService jwtSvc;
    private final CustomUserDetailsService userDetServ;


    public AuthFilter(JWTService jwtSvc,
                      CustomUserDetailsService userDetServ) {
        this.jwtSvc = jwtSvc;
        this.userDetServ = userDetServ;
    }



    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("AuthFilter hit! Header = " + request.getHeader("Authorization"));

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

//HTTP request comes with a header which contains the string as seen below, now that string the variable 'authHeader'
            String authHeader = request.getHeader("Authorization");

//checks if authHeader is null or doesn't start with bearer meaning no token
            if (authHeader == null
                    || !(authHeader.startsWith("Bearer "))) {

                filterChain.doFilter(request, response);

                return;
            }

//token removed from string by creating a substring
// from the 7th element in the string to the end, i.e "Bearer kwpmpidjpokklm",
//is in the 7th slot so a new string is created from that 7th element
            String token = authHeader.substring(7);

                if (!jwtSvc.isTokenValid(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or Expired Token");
                    return;
                }

                    String email = jwtSvc.extractEmail(token);

                    String name = jwtSvc.extractUsername(token);

                    String role = jwtSvc.extractRole(token);

//Gets the user and checks for its role and things it's allowed to do under said role
                //Represents a logged-in user
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

//Security context holder holds current authentication session (i.e user's credentials)
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                filterChain.doFilter(request, response);

            }
        }

    }


