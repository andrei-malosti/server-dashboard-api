package com.dashboard.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
	
	@Email
	@NotBlank(message = "field email is required")
	private String email;

	@NotBlank(message = "field name is required")
	private String name;
	
	@NotBlank(message = "field password is required")
	private String password;
	
}
