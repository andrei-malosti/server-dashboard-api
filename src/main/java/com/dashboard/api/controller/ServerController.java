package com.dashboard.api.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.api.dto.ServerRequestDTO;
import com.dashboard.api.infra.multitenancy.UserContext;
import com.dashboard.api.service.ServerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class ServerController {

	private final ServerService serverService;
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody @Valid ServerRequestDTO serverRequest){
		return ResponseEntity.status(HttpStatus.CREATED).body(serverService.create(serverRequest, UserContext.getUserId()));
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> findAllUserServer(@RequestParam(defaultValue = "", required = false) String searchTerm){
		return ResponseEntity.ok(serverService.findServerBySearchTerm(searchTerm, UserContext.getUserId()));
	}
	
	@GetMapping("/{serverId}")
	public ResponseEntity<?> findByServerId(@PathVariable UUID serverId){
		return ResponseEntity.ok(serverService.findByIdAndUserId(serverId, UserContext.getUserId()));
	}
	
	/*
	 * toDo: do toggle to change server status to offline and online
	 */
	
}
