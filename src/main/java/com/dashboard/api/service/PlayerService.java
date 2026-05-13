package com.dashboard.api.service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.AnwserDTO;
import com.dashboard.api.dto.PlayerRequestDTO;
import com.dashboard.api.dto.PlayerResponseDTO;
import com.dashboard.api.entity.Action;
import com.dashboard.api.entity.Player;
import com.dashboard.api.entity.PlayerStatus;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.PlayerRepository;
import com.dashboard.api.repository.ServerRepository;
import com.dashboard.api.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {

	private final PlayerRepository playerRepository;
	private final UserRepository userRepository;
	private final ServerRepository serverRepository;
	private final LogService logService;
	
	@Transactional
	public PlayerResponseDTO playerInvite(PlayerRequestDTO playerRequest, UUID serverId) {

		var server = serverRepository.findById(serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));

		if (playerRepository.existsBySteamIdAndServerId(playerRequest.getSteamId(), serverId))
			throw new BusinessException("Player already play in this server");

		var player = Player.builder().steamId(playerRequest.getSteamId()).nickname(playerRequest.getNickname())
				.playerStatus(PlayerStatus.PENDING).servers(new HashSet<>(Set.of(server)))
				.build();

		return PlayerResponseDTO.from(playerRepository.save(player), server.getName());
	}
	
	@Transactional
	public PlayerResponseDTO anwserInvite(UUID userId, UUID serverId, Long steamId, AnwserDTO isAccepted) {
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Resource not found");
		
		var player = playerRepository.findBySteamIdAndServerId(steamId, serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found"));

		if (isAccepted.getIsAccepted().equals(true)) {

			if (player.getPlayerStatus() == PlayerStatus.APPROVED)
				throw new BusinessException("The player is already approved");

			player.setPlayerStatus(PlayerStatus.APPROVED);

		} else {

			if (player.getPlayerStatus() == PlayerStatus.DENIED)
				throw new BusinessException("The player is already denied");

			player.setPlayerStatus(PlayerStatus.DENIED);
		}

		return PlayerResponseDTO.from(player);

	}
	
	@Transactional
	public PlayerResponseDTO banPlayer(UUID serverId, UUID userId, Long steamId) {
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Resource not found");
		
		var player = playerRepository.findBySteamIdAndServerId(steamId, serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found"));
		
		if(player.getPlayerStatus() == PlayerStatus.BANNED)
			throw new BusinessException("Player is already banned");
		
		player.setPlayerStatus(PlayerStatus.BANNED);
		
		var userProxy = userRepository.getReferenceById(userId);
		var serverProxy = serverRepository.getReferenceById(serverId);
		
		logService.createLog(
				serverProxy, 
				userProxy, 
				String.format("Player with id %s has been banned", player.getId().toString()) , 
				Action.BAN_PLAYER, 
				OffsetDateTime.now());
		
		return PlayerResponseDTO.from(player);
	}
	
	public Slice<PlayerResponseDTO> findServerPlayers(UUID serverId, UUID userId, PlayerStatus playerStatus, Pageable pageable){
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		return playerRepository.findServerPlayers(serverId, userId, playerStatus, pageable)
				.map(PlayerResponseDTO::from);
	}
	
}
