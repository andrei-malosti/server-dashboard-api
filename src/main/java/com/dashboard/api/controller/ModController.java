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
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.api.dto.ModRequestDTO;
import com.dashboard.api.infra.multitenancy.UserContext;
import com.dashboard.api.service.ModService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ModController {

	private final ModService modService;
	
	@PostMapping("/{serverId}/mods")
	public ResponseEntity<?> create(@RequestBody @Valid ModRequestDTO modRequest, @PathVariable UUID serverId){
		return ResponseEntity.status(HttpStatus.CREATED).body(modService.create(modRequest, serverId, UserContext.getUserId()));
	}
	
	@GetMapping("/{serverId}/mods")
	public ResponseEntity<?> findServerMods(@PathVariable UUID serverId, Pageable pageable){
		return ResponseEntity.ok(modService.findServerMods(serverId, UserContext.getUserId(), pageable));
	}
	
	@GetMapping("/{serverId}/mods/inactive")
	public ResponseEntity<?> findServerInactiveMods(@PathVariable UUID serverId, Pageable pageable){
		return ResponseEntity.ok(modService.findServerInactiveMods(serverId, UserContext.getUserId(), pageable));
	}
	
	@PatchMapping("/{serverId}/mods/{modStringId}/activate")
	public ResponseEntity<?> activate(@PathVariable UUID serverId, @PathVariable String modStringId){
		return ResponseEntity.status(HttpStatus.CREATED).body(modService.activate(serverId, UserContext.getUserId(), modStringId));
	}
	
	@PatchMapping("/{serverId}/mods/{modStringId}/deactivate")
	public ResponseEntity<?> deactivate(@PathVariable UUID serverId, @PathVariable String modStringId){
		return ResponseEntity.status(HttpStatus.CREATED).body(modService.deactivate(serverId, UserContext.getUserId(), modStringId));
	}
	
}
