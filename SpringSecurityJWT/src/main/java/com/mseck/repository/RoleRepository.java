package com.mseck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mseck.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(String name);
}
