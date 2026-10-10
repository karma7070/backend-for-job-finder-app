package com.FindAJob.demo.SecurityPackage;


import com.FindAJob.demo.companies.services.CompService;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import com.FindAJob.demo.reg_users.services.Reg_UsersService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Reg_UsersService userSrv;
    private final CompService compSrv;
    private static UserDetails user_n;


    @Autowired
    public CustomUserDetailsService(Reg_UsersService userSrv,
                                    CompService compSrv) {
        this.userSrv = userSrv;
        this.compSrv = compSrv;

    }


    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username)
            throws UsernameNotFoundException {

            Optional<Reg_Users> user =
                    userSrv.getUserByEmail(username);
            if (user.isPresent()) {
                user_n = user.get();
                return user.get();
            }

            Optional<Companies> company =
                    compSrv.getUserByEmail(username);
            if (company.isPresent()) {
                user_n = company.get();
                return company.get();
            }

            throw new UsernameNotFoundException("No account exists with this email!");

    }

    public UserDetails getUserDetail(){
        return user_n;
    }

}
