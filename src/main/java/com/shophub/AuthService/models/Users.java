package com.shophub.AuthService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@JsonDeserialize(as = Users.class)
public class Users extends BaseModel{
    private String email;
    private String password;
    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Role> roles=new HashSet<>();
}
