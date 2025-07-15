package com.shophub.AuthService.dtos;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String email;
    private String password;
}
