package com.dashboard.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, UUID>{
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public Optional<User> findByEmail(String email);
	
}
