package com.msj.myapp.auth;


import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//Login과 Profile의 필드값이 한 곳에 필요해서
public class SignupRequest {
    private String username;
    private String password;
    private String nickname;
    private String email;
}