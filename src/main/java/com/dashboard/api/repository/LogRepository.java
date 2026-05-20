package com.dashboard.api.repository;

import java.util.UUID;

import com.dashboard.api.entity.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, UUID>{

	@Query("SELECT l FROM Log l WHERE l.server.id = :serverId")
	public Slice<Log> findServerLogs(@Param("serverId") UUID serverId, Pageable pageable);
	
}
