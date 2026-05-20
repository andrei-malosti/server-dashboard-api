package com.dashboard.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dashboard.api.entity.Server;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {

	@Query("SELECT s FROM Server s JOIN s.users u WHERE u.id = :userId AND "
			+ "(lower(s.name) LIKE lower(concat('%', :searchTerm, '%')) OR " 
			+ "lower(s.gameName) LIKE lower(concat('%', :searchTerm, '%')))")
	public Slice<Server> findAllBySearchTerm(@Param("searchTerm") String searchTerm, @Param("userId") UUID userId, Pageable pageable);
	
	@Query("SELECT s FROM Server s WHERE "
			+ "(lower(s.name) LIKE lower(concat('%', :searchTerm, '%')) OR " 
			+ "lower(s.gameName) LIKE lower(concat('%', :searchTerm, '%')))")
	public Slice<Server> findAllBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

	@Query("SELECT s FROM Server s JOIN s.users u WHERE u.id = :userId AND s.id = :serverId")
	public Optional<Server> findByIdAndUserId(@Param("serverId") UUID serverId, @Param("userId") UUID userId);

	@Query("SELECT COUNT(s) > 0 FROM Server s JOIN s.users u WHERE s.port = :port AND u.id = :userId")
	public boolean existsByPortAndUserId(@Param("port") String port, @Param("userId") UUID userId);
	
	@Query("SELECT COUNT(s) > 0 FROM Server s JOIN s.users u WHERE s.id = :serverId AND u.id = :userId")
	public boolean existsByIdAndUserId(@Param("serverId") UUID serverId, @Param("userId") UUID userId);
}
