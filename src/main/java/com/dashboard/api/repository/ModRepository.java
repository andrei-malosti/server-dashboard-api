package com.dashboard.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.ServerMod;

@Repository
public interface ModRepository extends JpaRepository<ServerMod, UUID>{

	@Query("SELECT COUNT(m) > 0 FROM ServerMod m WHERE m.server.id = :serverId AND m.activationId = :activationId")
	public boolean existsInServer(@Param("serverId") UUID serverId, @Param("activationId") String activationId);
	
	@Query("SELECT m FROM ServerMod m WHERE m.server.id = :serverId")
	public Slice<ServerMod> findServerMods(@Param("serverId") UUID serverId);
	
}
