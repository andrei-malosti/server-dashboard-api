package com.dashboard.api.dto;

import java.util.UUID;

import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponseDTO {

	private UUID id;
	private String name;
	private String ip;
	private String port;
	private String gameName;
	private Status status;
	
	public static ServerResponseDTO from(Server server) {
		return ServerResponseDTO.builder()
				.id(server.getId())
				.name(server.getName())
				.ip(server.getIp())
				.port(server.getPort())
				.gameName(server.getGameName())
				.status(server.getStatus())
				.build();
	}
	
	public static ServerResponseDTO fromWithoutIpPort(Server server) {
		return ServerResponseDTO.builder()
				.id(server.getId())
				.name(server.getName())
				.gameName(server.getGameName())
				.status(server.getStatus())
				.build();
	}
	
}
