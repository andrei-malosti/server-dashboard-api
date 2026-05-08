package com.dashboard.api.entity;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Server {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@EqualsAndHashCode.Include
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String ip;
	
	@Column(nullable = false)
	private String port;
	
	@Column(nullable = false)
	private String gameName;
	
	@ManyToMany
	@JoinTable(name = "server_user",
	joinColumns = @JoinColumn(name = "server_id"),
	inverseJoinColumns = @JoinColumn(name = "user_id"))
	@Builder.Default
	private Set<User> users = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = "server_player",
	joinColumns = @JoinColumn(name = "server_id"),
	inverseJoinColumns = @JoinColumn(name = "player_id"))
	@Builder.Default
	private Set<Player> players = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
}
