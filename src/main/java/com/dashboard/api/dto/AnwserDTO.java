package com.dashboard.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnwserDTO {
	
	@NotNull(message = "you must accept or deny")
	private Boolean isAccepted;

}
