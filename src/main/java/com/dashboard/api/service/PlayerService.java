package com.dashboard.api.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.AnwserDTO;
import com.dashboard.api.dto.PlayerRequestDTO;
import com.dashboard.api.dto.PlayerResponseDTO;
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
	
	@Transactional
	public PlayerResponseDTO playerInvite(PlayerRequestDTO playerRequest, UUID serverId) {

		var server = serverRepository.findById(serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));

		if (playerRepository.findByIdAndServerId(playerRequest.getSteamId(), server.getId()).isPresent())
			throw new BusinessException("Player already play in this server");

		var player = Player.builder().steamId(playerRequest.getSteamId()).nickname(playerRequest.getNickname())
				.playerStatus(PlayerStatus.PENDING).servers(new HashSet<>(Set.of(server)))
				.build();
		
		server.getPlayers().add(player);

		return PlayerResponseDTO.from(playerRepository.save(player), server.getName());
	}
	
	@Transactional
	public PlayerResponseDTO anwserInvite(UUID userId, UUID serverId, Long steamId, AnwserDTO isAccepted) {
		userRepository.findByIdAndServerId(userId, serverId).orElseThrow(
				() -> new BusinessException("Only admins and moderators can accept a player into their servers"));

		var player = playerRepository.findByIdAndServerId(steamId, serverId)
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
		userRepository.findByIdAndServerId(userId, serverId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		var player = playerRepository.findByIdAndServerId(steamId, serverId)
				.orElseThrow(() -> new ResourceNotFoundException("Player not found"));
		
		if(player.getPlayerStatus() == PlayerStatus.BANNED)
			throw new BusinessException("Player is already banned");
		
		player.setPlayerStatus(PlayerStatus.BANNED);
		return PlayerResponseDTO.from(player);
	}
	
	public Slice<PlayerResponseDTO> findServerPlayers(UUID serverId, UUID userId, PlayerStatus playerStatus, Pageable pageable){
		var server = serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		return playerRepository.findServerPlayers(server.getId(), userId, playerStatus, pageable)
				.map(PlayerResponseDTO::from);
	}
	
}
