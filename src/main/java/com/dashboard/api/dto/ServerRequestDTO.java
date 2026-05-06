package com.dashboard.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerRequestDTO {

	@NotBlank(message = "field name is required")
	private String name;
	
	@NotBlank(message = "field ip is required")
	private String ip;

	@NotBlank(message = "field port is required")
	private String port;
	
	@NotBlank(message = "field game name is required")
	private String gameName;
	
}
