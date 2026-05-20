package com.dashboard.api.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
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
	
	@ManyToMany(mappedBy = "servers")
	@Builder.Default
	private Set<User> users = new HashSet<>();

	@ManyToMany(mappedBy = "servers")
	@Builder.Default
	private Set<Player> players = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	
}
