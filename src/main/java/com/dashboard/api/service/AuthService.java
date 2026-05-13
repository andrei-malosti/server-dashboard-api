package com.dashboard.api.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.LoginRequestDTO;
import com.dashboard.api.dto.LoginResponseDTO;
import com.dashboard.api.dto.RegisterRequestDTO;
import com.dashboard.api.dto.RegisterResponseDTO;
import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.infra.security.JwtService;
import com.dashboard.api.infra.security.userdetails.CustomUserDetails;
import com.dashboard.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final AuthenticationManager authManager;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	
	public LoginResponseDTO login(LoginRequestDTO loginRequest) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
		var auth = authManager.authenticate(usernamePassword);
		CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		String token = jwtService.generateToken(userDetails);
		
		return LoginResponseDTO.builder()
				.token(token)
				.build();
	}
	
	public RegisterResponseDTO register(RegisterRequestDTO registerRequest) {
		if(userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new BusinessException("Email already in use");
		}
		User newUser = User.builder()
		.email(registerRequest.getEmail())
		.name(registerRequest.getName())
		.password(passwordEncoder.encode(registerRequest.getPassword()))
		.role(Role.ADMIN)
		.build();
		
		return RegisterResponseDTO.from(userRepository.save(newUser));
	}
}
