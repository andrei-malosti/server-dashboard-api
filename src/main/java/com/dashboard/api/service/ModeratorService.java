package com.dashboard.api.service;

import java.util.HashSet;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.ModeratorResponseDTO;
import com.dashboard.api.dto.RegisterRequestDTO;
import com.dashboard.api.entity.Role;
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
	
	@Transactional
	public ModeratorResponseDTO create(RegisterRequestDTO moderatorRequest, UUID userId, UUID serverId) {
		var server = serverRepository.findByIdAndUserId(serverId, userId)
		.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		if(moderatorRepository.findByEmail(moderatorRequest.getEmail()).isPresent())
			throw new BusinessException("Email already in use");
		
		var moderator = buildModerator(moderatorRequest);
		moderator.getServers().add(server);
		server.getUsers().add(moderator);
		return ModeratorResponseDTO.from(moderatorRepository.save(moderator), server.getId(), server.getName());
	}
	
	public Slice<ModeratorResponseDTO> findServerModerators(UUID serverId, UUID userId, Pageable pageable){
		serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("server not found"));
		
		return moderatorRepository.findServerModerators(serverId, pageable)
				.map(ModeratorResponseDTO::from);
	}
	
	private User buildModerator(RegisterRequestDTO moderatorRequest) {
		return User.builder()
				.email(moderatorRequest.getEmail())
				.name(moderatorRequest.getName())
				.password(passwordEncoder.encode(moderatorRequest.getPassword()))
				.role(Role.MODERATOR)
				.servers(new HashSet<>())
				.build();
	}

}
