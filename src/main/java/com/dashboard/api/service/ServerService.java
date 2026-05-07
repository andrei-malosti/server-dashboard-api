package com.dashboard.api.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.PlayerRequestDTO;
import com.dashboard.api.dto.PlayerResponseDTO;
import com.dashboard.api.dto.ServerRequestDTO;
import com.dashboard.api.dto.ServerResponseDTO;
import com.dashboard.api.entity.Player;
import com.dashboard.api.entity.PlayerStatus;
import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.Status;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.PlayerRepository;
import com.dashboard.api.repository.ServerRepository;
import com.dashboard.api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerService {

	private final ServerRepository serverRepository;
	private final UserRepository userRepository;
	private final PlayerRepository playerRepository;
	
	@Transactional
	public ServerResponseDTO create(ServerRequestDTO serverRequest, UUID userId) {
		if(serverRepository.existsByPortAndUserId(serverRequest.getPort(), userId))
			throw new BusinessException("Server with that port already exists");
		
		var serverOwner = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		if(serverOwner.getRole() == Role.MODERATOR)
			throw new BusinessException("Only the admin can create new servers");
		
		var server = buildServer(serverRequest, serverOwner);
		serverOwner.getServers().add(server);
		return ServerResponseDTO.from(serverRepository.save(server));
	}
	
	public Slice<ServerResponseDTO> findServerBySearchTerm(String searchTerm, UUID userId){
		return serverRepository.findAllBySearchTerm(searchTerm, userId)
				.map(ServerResponseDTO::from);
	}
	
	public Slice<ServerResponseDTO> findServerBySearchTerm(String searchTerm){
		return serverRepository.findAllBySearchTerm(searchTerm)
				.map(ServerResponseDTO::from);
	}
	
	public ServerResponseDTO findByIdAndUserId(UUID serverId, UUID userId) {
		return ServerResponseDTO.from(serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found")));
	}
	
	@Transactional
	public ServerResponseDTO toggle(UUID serverId, UUID userId) {
		var server =  serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		if(server.getStatus() == Status.OFFLINE) {
			server.setStatus(Status.ONLINE);

		} else {
			server.setStatus(Status.OFFLINE);
		}
		
		return ServerResponseDTO.from(server);
	}
	
	@Transactional
	public PlayerResponseDTO playerInvite(PlayerRequestDTO playerRequest, UUID serverId) {
		
		Server server = serverRepository.findById(serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		if(playerRepository.findByIdAndServerId(playerRequest.getSteamId(), server.getId()).isPresent())
			throw new BusinessException("Player already play in this server");
		
		Player player = Player.builder()
		.steamId(playerRequest.getSteamId())
		.nickname(playerRequest.getNickname())
		.playerStatus(PlayerStatus.PENDING)
		.server(server)
		.build();
		
		return PlayerResponseDTO.from(playerRepository.save(player));
	}
	
	@Transactional
	public PlayerResponseDTO acceptPlayer(UUID userId, UUID serverId, Long steamId) {
		userRepository.findByIdAndServerId(userId, serverId)
				.orElseThrow(() -> new BusinessException("Only admins and moderators can accept a player into their servers"));
		
		Player player = playerRepository.findByIdAndServerId(steamId, serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found"));
		
		if(player.getPlayerStatus() == PlayerStatus.APPROVED)
			throw new BusinessException("The player is already approved");
		
		player.setPlayerStatus(PlayerStatus.APPROVED);
		return PlayerResponseDTO.from(player);
		
	}
	
	private Server buildServer(ServerRequestDTO serverRequest, User serverOwner) {
		return Server.builder()
				.name(serverRequest.getName())
				.ip(serverRequest.getIp())
				.port(serverRequest.getPort())
				.gameName(serverRequest.getGameName())
				.users(new HashSet<>(Set.of(serverOwner)))
				.status(Status.OFFLINE)
				.build();
	}
	
}
