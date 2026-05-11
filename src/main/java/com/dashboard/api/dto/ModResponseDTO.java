package com.dashboard.api.dto;

import java.util.UUID;

import com.dashboard.api.entity.ServerMod;
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
public class ModResponseDTO {
	
	private UUID id;
	private String modName;
	private String activationId;
	private Boolean isActive;
	private String serverName;
	private UUID serverId;
	
	public static ModResponseDTO from(ServerMod mod) {
		return ModResponseDTO.builder()
				.id(mod.getId())
				.activationId(mod.getActivationId())
				.modName(mod.getModName())
				.isActive(mod.getIsActive())
				.serverName(mod.getServer().getName())
				.serverId(mod.getServer().getId())
				.build();
	}
}
