package com.dashboard.api.entity;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
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

	@ManyToMany
	@JoinTable(
			name = "server_players",
			joinColumns = @JoinColumn(name = "player_id"),
			inverseJoinColumns = @JoinColumn(name = "server_id")
	)
	private Set<Server> servers = new HashSet<>();
	
}
