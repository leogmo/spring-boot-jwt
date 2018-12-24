package com.concrete.challenge.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concrete.challenge.users.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
}