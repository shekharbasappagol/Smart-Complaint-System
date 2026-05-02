package com.complaint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.complaint.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}