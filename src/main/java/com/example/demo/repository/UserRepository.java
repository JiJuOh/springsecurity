package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UserEntity;

public interface UserRepository extends JpaRepository< UserEntity, Integer >{ //integer 로하면 에러남. 객체타입으로 명시

	boolean existsByUsername(String username);
	
	
	UserEntity findByUsername(String username);
}
