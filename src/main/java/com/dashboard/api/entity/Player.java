package com.dashboard.api.entity;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	
	@Column(nullable = false)
	private Long steamId;
	
	@Column(nullable = false)
	private String nickname;
	
	private LocalTime timePlayed;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PlayerStatus playerStatus;
	
	@ManyToMany(mappedBy = "players")
	@Builder.Default
	private Set<Server> servers = new HashSet<>();
	
}
