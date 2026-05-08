package com.dashboard.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.Player;
import com.dashboard.api.entity.PlayerStatus;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID>{

	@Query("SELECT p FROM Player p JOIN p.servers s WHERE s.id = :serverId AND p.steamId = :steamId")
	public Optional<Player> findByIdAndServerId(@Param("steamId") Long steamId, @Param("serverId") UUID serverId);
	
	@Query("SELECT p FROM Player p JOIN p.servers s JOIN s.users u WHERE s.id = :serverId AND u.id = :userId AND p.playerStatus = :status")
	public Slice<Player> findServerPlayers(@Param("serverId") UUID serverId, @Param("userId") UUID userId, @Param("status") PlayerStatus status, Pageable pageable);
}
