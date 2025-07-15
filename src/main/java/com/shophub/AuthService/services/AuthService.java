package com.shophub.AuthService.services;

import com.shophub.AuthService.exceptions.UserAlreadyExistException;
import com.shophub.AuthService.models.Users;
import com.shophub.AuthService.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public Boolean signUp(String email, String password){
        if(userRepository.findByEmail(email).isPresent()){
            throw new UserAlreadyExistException("User with email "+email+" : Already Exist");
        }
        Users newUser=new Users();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(newUser);
        return true;
    }
    public String login(String email,String password){
        return "success";
    }
}
