package com.FindAJob.demo.reg_users.requests;

import com.FindAJob.demo.SecurityPackage.UserRoles;
import com.FindAJob.demo.reg_users.publicenums.Gen_Type;

public record Reg_UserRequestDTO(
        String name,
        Integer age,
        Gen_Type gender,
        String profession,
        String password,
        String confPassword,
        String email,
        UserRoles role
) {
}
