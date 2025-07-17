package com.shophub.AuthService.services;

import com.shophub.AuthService.exceptions.UserAlreadyExistException;
import com.shophub.AuthService.exceptions.UserNotFountException;
import com.shophub.AuthService.exceptions.WrongPasswordException;
import com.shophub.AuthService.models.Role;
import com.shophub.AuthService.models.Session;
import com.shophub.AuthService.models.SessionStatus;
import com.shophub.AuthService.models.Users;
import com.shophub.AuthService.repositories.SessionRepository;
import com.shophub.AuthService.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService {

    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

//    private SecretKey key=Jwts.SIG.HS256.key().build(); //Inbuild secrete key

    //For custom key
    private SecretKey key= Keys.hmacShaKeyFor(
            "Nafeesisveryveryveryveryveryveryveryveryveryveryveryverycool"
                    .getBytes(StandardCharsets.UTF_8));



    public AuthService(UserRepository userRepository,BCryptPasswordEncoder passwordEncoder,SessionRepository sessionRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.sessionRepository=sessionRepository;
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
        Optional<Users> users=userRepository.findByEmail(email);
        if(users.isEmpty()){
            throw new UserNotFountException("User with email :"+email+" not exist");
        }
        if(passwordEncoder.matches(password,users.get().getPassword())){
          String token= createJwtToken(users.get().getId(),users.get().getRoles(),email);
          //Store the token into db
            Session session=new Session();
            session.setToken(token);
            Calendar calendar=Calendar.getInstance();
            Date currentData=calendar.getTime();

            calendar.add(Calendar.DAY_OF_MONTH,30);
            Date datePlus30Days=calendar.getTime();
            session.setExpireAt(datePlus30Days);
            session.setUser(users.get());
            session.setSessionStatus(SessionStatus.ACTIVE);
            sessionRepository.save(session);
          return token;
        }else{
            throw new WrongPasswordException("Wrong password");
        }
    }

    private String createJwtToken(Long id, Set<Role> roles, String email){
        Map<String,Object> dataINJwt=new HashMap<>();
        dataINJwt.put("user_id",id);
        dataINJwt.put("roles",roles);
        dataINJwt.put("email",email);

        Calendar calendar=Calendar.getInstance();
        Date currentData=calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH,30);
        Date datePlus30Days=calendar.getTime();

        String token= Jwts.builder()
                .claims(dataINJwt)
                .expiration(datePlus30Days)
                .issuedAt(currentData)
                .signWith(key)
                .compact();
        return token;
    }

    public Boolean validate(String token) {
        try{
            Jws<Claims> claims= Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            Date expiryAt=claims.getPayload().getExpiration();
            Long id=claims.getPayload().get("user_id",Long.class);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
