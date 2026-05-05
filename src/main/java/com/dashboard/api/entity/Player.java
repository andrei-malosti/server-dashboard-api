package com.dashboard.api.entity;

import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Id
	@Column(nullable = false)
	private Long steamId;
	
	@Column(nullable = false)
	private String nickname;
	
	@Column(nullable = false)
	private LocalTime timePlayed;
	
	@Column(nullable = false)
	private Boolean isBanned;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Server server;
	
}
