package com.dashboard.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.ServerMod;

@Repository
public interface ModRepository extends JpaRepository<ServerMod, UUID>{

	@Query("SELECT COUNT(m) > 0 FROM ServerMod m WHERE m.server.id = :serverId AND m.modStringId = :modStringId")
	public boolean existsInServer(@Param("serverId") UUID serverId, @Param("modStringId") String modStringId);
	
	@Query("SELECT m FROM ServerMod m WHERE m.server.id = :serverId AND (:isActive IS NULL OR m.isActive = :isActive)")
	public Slice<ServerMod> findServerMods(@Param("serverId") UUID serverId, @Param("isActive") Boolean isActive, Pageable pageable);
	
	@Query("SELECT m FROM ServerMod m WHERE m.server.id = :serverId AND m.modStringId = :modStringId AND m.isActive = :isActive")
	public Optional<ServerMod> findModByModStringId(@Param("serverId") UUID serverId, @Param("modStringId") String modStringId, @Param("isActive") Boolean isActive);
	
	
}
