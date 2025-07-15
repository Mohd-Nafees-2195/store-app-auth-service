package com.shophub.AuthService.repositories;

import com.shophub.AuthService.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Long> {
}
