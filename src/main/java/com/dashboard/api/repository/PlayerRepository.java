package com.dashboard.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID>{

	@Query("SELECT p FROM Player p WHERE p.steamId = :steamId AND p.server.id = :serverId")
	public Optional<Player> findByIdAndServerId(@Param("steamId") Long steamId, @Param("serverId") UUID serverId);
	
}
