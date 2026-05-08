package com.dashboard.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.api.dto.AnwserDTO;
import com.dashboard.api.dto.PlayerRequestDTO;
import com.dashboard.api.entity.PlayerStatus;
import com.dashboard.api.infra.multitenancy.UserContext;
import com.dashboard.api.service.PlayerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerPlayerController {
	
	private final PlayerService playerService;
	
	@PostMapping("/{serverId}/whitelist/apply")
	public ResponseEntity<?> applyToWhitelist(@RequestBody @Valid PlayerRequestDTO playerRequest, @PathVariable UUID serverId){
		return ResponseEntity.status(HttpStatus.CREATED).body(playerService.playerInvite(playerRequest, serverId));
	}
	
	@PatchMapping("/{serverId}/player/{steamId}/ban")
	public ResponseEntity<?> banPlayer(@PathVariable UUID serverId, @PathVariable Long steamId){
		return ResponseEntity.ok(playerService.banPlayer(serverId, UserContext.getUserId(), steamId));
	}

	@PatchMapping("/{serverId}/whitelist/{steamId}")
	public ResponseEntity<?> awnserInvite(@PathVariable UUID serverId, @PathVariable Long steamId, @RequestBody AnwserDTO anwser){
		return ResponseEntity.ok(playerService.anwserInvite(UserContext.getUserId(), serverId, steamId, anwser));
	}
	
	@GetMapping("/{serverId}/players")
	public ResponseEntity<?> findServerPlayers(@PathVariable UUID serverId, 
			@RequestParam(defaultValue = "APPROVED", required = true) PlayerStatus status, 
			Pageable pageable){
		return ResponseEntity.ok(playerService.findServerPlayers(serverId, UserContext.getUserId(), status, pageable));
	}
}
