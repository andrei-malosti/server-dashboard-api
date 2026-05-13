package com.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.LogResponseDTO;
import com.dashboard.api.entity.Action;
import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.ServerLog;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.LogRepository;
import com.dashboard.api.repository.ServerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	
	private final LogRepository logRepository;
	private final ServerRepository serverRepository;

	@Transactional
	public void createLog(Server server, 
			User user,
			String description,
			Action action,
			OffsetDateTime dateTime
			) {
		
		var log = ServerLog.builder()
		.description(description)
		.action(action)
		.dateTime(dateTime)
		.user(user)
		.server(server)
		.build();
		
		logRepository.save(log);
	}
	
	public Slice<LogResponseDTO> findServerLogs(UUID serverId, UUID userId, Pageable pageable){
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Resource not found");
		
		return logRepository.findServerLogs(serverId, pageable)
				.map(LogResponseDTO::from);
	}
	
}
