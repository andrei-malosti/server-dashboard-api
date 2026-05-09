package com.dashboard.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.api.dto.RegisterRequestDTO;
import com.dashboard.api.infra.multitenancy.UserContext;
import com.dashboard.api.service.ModeratorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerModeratorController {

	private final ModeratorService moderatorService;
	
	@PostMapping("{serverId}/moderators")
	public ResponseEntity<?> create(@RequestBody @Valid RegisterRequestDTO registerRequest, @PathVariable UUID serverId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(moderatorService.create(registerRequest, UserContext.getUserId(), serverId));
	}
	
	@GetMapping("{serverId}/moderators")
	public ResponseEntity<?> findServerModerators(@PathVariable UUID serverId, Pageable pageable){
		return ResponseEntity.ok(moderatorService.findServerModerators(serverId, UserContext.getUserId(), pageable));
	}
	
}
