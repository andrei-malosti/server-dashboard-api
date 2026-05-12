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
public class ModRequestDTO {

	@NotBlank(message = "mod name is required")
	private String modName;
	
	@NotBlank(message = "mod string id is required")
	private String modStringId;
}
