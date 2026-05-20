package com.dashboard.api.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.ServerRequestDTO;
import com.dashboard.api.dto.ServerResponseDTO;
import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.Status;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.ServerRepository;
import com.dashboard.api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerService {

	private final ServerRepository serverRepository;
	private final UserRepository userRepository;

	@Transactional
	public ServerResponseDTO create(ServerRequestDTO serverRequest, UUID userId) {

		var serverOwner = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (serverRepository.existsByPortAndUserId(serverRequest.getPort(), userId))
			throw new BusinessException("Server with that port already exists");

		if (serverOwner.getRole() == Role.MODERATOR)
			throw new BusinessException("Only the admin can create new servers");

		var server = serverRepository.save(buildServer(serverRequest, serverOwner));
		serverOwner.getServers().add(server);
				
		return ServerResponseDTO.from(server);
	}

	public Slice<ServerResponseDTO> findUserServerBySearchTerm(String searchTerm, UUID userId, Pageable pageable) {
		return serverRepository.findAllBySearchTerm(searchTerm, userId, pageable).map(ServerResponseDTO::from);
	}

	public Slice<ServerResponseDTO> findServerBySearchTerm(String searchTerm, Pageable pageable) {
		return serverRepository.findAllBySearchTerm(searchTerm, pageable).map(ServerResponseDTO::fromWithoutIpPort);
	}

	public ServerResponseDTO findByIdAndUserId(UUID serverId, UUID userId) {
		return ServerResponseDTO.from(serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found")));
	}

	@Transactional
	public ServerResponseDTO toggle(UUID serverId, UUID userId) {
		var server = serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));

		if (server.getStatus() == Status.OFFLINE) {
			server.setStatus(Status.ONLINE);

		} else {
			server.setStatus(Status.OFFLINE);
		}

		return ServerResponseDTO.from(server);
	}

	private Server buildServer(ServerRequestDTO serverRequest, User serverOwner) {
		return Server.builder().name(serverRequest.getName()).ip(serverRequest.getIp()).port(serverRequest.getPort())
				.gameName(serverRequest.getGameName()).users(Set.of(serverOwner)).status(Status.OFFLINE)
				.build();
	}

}
