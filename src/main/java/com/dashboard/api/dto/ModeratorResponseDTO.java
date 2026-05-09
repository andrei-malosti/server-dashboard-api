package com.dashboard.api.dto;

import java.util.UUID;

import com.dashboard.api.entity.User;
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
public class ModeratorResponseDTO {

	private String email;
	private String name;
	private UUID serverId;
	private String serverName;
	
	public static ModeratorResponseDTO from(User user) {
		return ModeratorResponseDTO.builder()
				.email(user.getEmail())
				.name(user.getName())
				.build();
	}
	
	public static ModeratorResponseDTO from(User user, UUID serverId, String serverName) {
		return ModeratorResponseDTO.builder()
				.email(user.getEmail())
				.name(user.getName())
				.serverId(serverId)
				.serverName(serverName)
				.build();
	}
	
}
