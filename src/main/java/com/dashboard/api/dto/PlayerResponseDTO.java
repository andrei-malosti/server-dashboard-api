package com.dashboard.api.dto;

import java.time.LocalTime;
import java.util.UUID;

import com.dashboard.api.entity.Player;
import com.dashboard.api.entity.PlayerStatus;
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
public class PlayerResponseDTO {

	private UUID id;
	private Long steamId;
	private String nickname;
	private LocalTime timePlayed;
	private PlayerStatus status;
	private String serverName;
	
	
	public static PlayerResponseDTO from(Player player) {
		return PlayerResponseDTO.builder()
				.id(player.getId())
				.steamId(player.getSteamId())
				.nickname(player.getNickname())
				.timePlayed(player.getTimePlayed())
				.status(player.getPlayerStatus())
				.serverName(player.getServer().getName())
				.build();
	}
}
