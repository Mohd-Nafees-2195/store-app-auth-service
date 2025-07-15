package com.shophub.AuthService.controllers;

import com.shophub.AuthService.dtos.*;
import com.shophub.AuthService.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<SignupResponseDto> signUp(@RequestBody SignupRequestDto request){
        SignupResponseDto response=new SignupResponseDto();
        if(authService.signUp(request.getEmail(), request.getPassword())){
            response.setRequestStatus(RequestStatus.SUCCESS);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else {
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        String token=authService.login(request.getEmail(), request.getPassword());
        LoginResponseDto responseDto=new LoginResponseDto();
        responseDto.setRequestStatus(RequestStatus.SUCCESS);

        MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
        headers.add("AUTH_TOKEN",token);

        ResponseEntity<LoginResponseDto> response=new ResponseEntity<>(
                responseDto,headers, HttpStatus.OK
        );
        return response;

    }
}
