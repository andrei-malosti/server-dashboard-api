package com.dashboard.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.dashboard.api.entity.Action;
import com.dashboard.api.entity.ServerLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogResponseDTO {

	private UUID id;
	private String description;
	private Action action;
	private OffsetDateTime dateTime;
	private String moderatorName;
	private UUID moderatorId;
	private String serverName;
	private UUID serverId;
	
public static LogResponseDTO from(ServerLog log) {
		
		return LogResponseDTO.builder()
				.id(log.getId())
				.description(log.getDescription())
				.action(log.getAction())
				.dateTime(log.getDateTime())
				.moderatorName(log.getUser().getName())
				.moderatorId(log.getUser().getId())
				.serverName(log.getServer().getName())
				.serverId(log.getServer().getId())
				.build();
	}	
}
