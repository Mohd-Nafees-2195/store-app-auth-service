package com.shophub.AuthService.services;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public Boolean signUp(String email, String password){
        return true;
    }
    public String login(String email,String password){
        return "success";
    }
}
