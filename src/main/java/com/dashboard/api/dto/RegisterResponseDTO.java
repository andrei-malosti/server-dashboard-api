package com.dashboard.api.dto;

import java.util.UUID;

import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {

	private UUID id;
	private String email;
	private String name;
	private Role role;
	
	public static RegisterResponseDTO from(User user) {
		return RegisterResponseDTO.builder()
				.id(user.getId())
				.email(user.getEmail())
				.name(user.getName())
				.role(user.getRole())
				.build();
	}
	
}
