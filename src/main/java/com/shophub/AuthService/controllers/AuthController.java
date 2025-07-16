package com.shophub.AuthService.controllers;

import com.shophub.AuthService.dtos.*;
import com.shophub.AuthService.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
        if(authService.signUp(request .getEmail(), request.getPassword())){
            response.setRequestStatus(RequestStatus.SUCCESS);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else {
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        LoginResponseDto responseDto=new LoginResponseDto();
        try {
            String token=authService.login(request.getEmail(), request.getPassword());
            responseDto.setRequestStatus(RequestStatus.SUCCESS);

            MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
            headers.add("AUTH_TOKEN",token);

            ResponseEntity<LoginResponseDto> response=new ResponseEntity<>(
                    responseDto,headers, HttpStatus.OK
            );
            return response;
        }catch (Exception e){
            responseDto.setRequestStatus(RequestStatus.FAILURE);
            ResponseEntity<LoginResponseDto> response=new ResponseEntity<>(
                    responseDto,null, HttpStatus.OK
            );
            return response;
        }
    }

    @GetMapping("/validate")
    public Boolean validateToken(@PathVariable("token") String token){
        return authService.validate(token);
    }
//    @GetMapping
//    public String test(){
//        return "Success";
//    }
}
