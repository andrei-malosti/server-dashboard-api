package com.dashboard.api.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TenantId;

@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mods")
public class Mod {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false)
	private String modStringId;
	
	@Column(nullable = false)
	private String modName;
	
	@Column(nullable = false)
	private Boolean isActive;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Server server;
	
}
