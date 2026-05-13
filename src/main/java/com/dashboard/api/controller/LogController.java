package com.dashboard.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dashboard.api.infra.multitenancy.UserContext;
import com.dashboard.api.service.LogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
public class LogController {

	private final LogService logService;
	
	@GetMapping("/{serverId}/logs")
	public ResponseEntity<?> findServerLogs(@PathVariable UUID serverId, Pageable pageable){
		return ResponseEntity.ok(logService.findServerLogs(serverId, UserContext.getUserId(), pageable));
	}
	
}
