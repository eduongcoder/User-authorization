package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {

}
