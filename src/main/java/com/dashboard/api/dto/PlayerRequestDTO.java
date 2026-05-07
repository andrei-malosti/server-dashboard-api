package com.dashboard.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRequestDTO {

	@NotNull(message = "steam id is required")
	private Long steamId;
	
	@NotBlank(message = "name is required")
	private String nickname;
	
}
