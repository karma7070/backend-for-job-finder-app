package com.FindAJob.demo.refreshtoken.response;

public record RefreshResDTO(String email,
                            String token,
                            String refToken

) {
/*
    public static RefreshResDTO from(RefreshToken refreshT, Reg_Users user){
        return new RefreshResDTO(
                user.getEmail(),
                token,
                refreshT.getToken()

        );

    }
*/
}
