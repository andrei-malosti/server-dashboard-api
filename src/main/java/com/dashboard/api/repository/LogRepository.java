package com.dashboard.api.repository;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.ServerLog;

@Repository
public interface LogRepository extends JpaRepository<ServerLog, UUID>{

	@Query("SELECT l FROM ServerLog l WHERE l.server.id = :serverId")
	public Slice<ServerLog> findServerLogs(@Param("serverId") UUID serverId, Pageable pageable);
	
}
