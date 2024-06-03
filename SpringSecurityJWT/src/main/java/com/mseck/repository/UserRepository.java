package com.mseck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mseck.model.UserEntity;
import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
	Boolean existsByUsername(String username);
}
