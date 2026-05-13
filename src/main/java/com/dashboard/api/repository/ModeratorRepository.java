package com.dashboard.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.User;

@Repository
public interface ModeratorRepository extends JpaRepository<User, UUID>{
	
	@Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
	public Boolean existsByEmail(@Param("email") String email);

	@Query("SELECT m FROM User m JOIN m.servers s WHERE s.id = :serverId")
	public Slice<User> findServerModerators(@Param("serverId") UUID serverId, Pageable pageable);
	
}
