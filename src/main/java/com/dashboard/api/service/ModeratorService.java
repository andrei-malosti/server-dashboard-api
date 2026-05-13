package com.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.ModeratorResponseDTO;
import com.dashboard.api.dto.RegisterRequestDTO;
import com.dashboard.api.entity.Action;
import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.ModeratorRepository;
import com.dashboard.api.repository.ServerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModeratorService {
	
	private final ModeratorRepository moderatorRepository;
	private final ServerRepository serverRepository;
	private final PasswordEncoder passwordEncoder;
	private final LogService logService;
	
	@Transactional
	public ModeratorResponseDTO create(RegisterRequestDTO moderatorRequest, UUID userId, UUID serverId) {
		var server = serverRepository.findByIdAndUserId(serverId, userId)
		.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		if(moderatorRepository.existsByEmail(moderatorRequest.getEmail()))
			throw new BusinessException("Email already in use");
		
		var moderator = moderatorRepository.save(buildModerator(moderatorRequest, server));
		
		logService.createLog(
				server, 
				moderator, 
				String.format("New moderator created with id %s", moderator.getId().toString()), 
				Action.CREATE_MODERATOR, 
				OffsetDateTime.now());
		
		return ModeratorResponseDTO.from(moderator, server.getId(), server.getName());
	}
	
	public Slice<ModeratorResponseDTO> findServerModerators(UUID serverId, UUID userId, Pageable pageable){
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		return moderatorRepository.findServerModerators(serverId, pageable)
				.map(ModeratorResponseDTO::from);
	}
	
	private User buildModerator(RegisterRequestDTO moderatorRequest, Server server) {
		return User.builder()
				.email(moderatorRequest.getEmail())
				.name(moderatorRequest.getName())
				.password(passwordEncoder.encode(moderatorRequest.getPassword()))
				.role(Role.MODERATOR)
				.servers(new HashSet<>(Set.of(server)))
				.build();
	}

}
