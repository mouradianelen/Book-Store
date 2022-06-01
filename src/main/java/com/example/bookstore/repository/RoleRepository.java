package com.example.bookstore.repository;

import com.example.bookstore.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole,Long> {
    UserRole findByName(String name);
}
