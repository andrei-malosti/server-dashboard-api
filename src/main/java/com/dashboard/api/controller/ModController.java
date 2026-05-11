package com.dashboard.api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<?> findServerMods(@PathVariable UUID serverId){
		return ResponseEntity.ok(modService.findServerMods(serverId, UserContext.getUserId()));
	}
	
}
