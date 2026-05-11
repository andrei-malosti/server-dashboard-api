package com.dashboard.api.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.ModRequestDTO;
import com.dashboard.api.dto.ModResponseDTO;
import com.dashboard.api.entity.ServerMod;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.ModRepository;
import com.dashboard.api.repository.ServerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModService {

	private final ModRepository modRepository;
	private final ServerRepository serverRepository;
	
	@Transactional
	public ModResponseDTO create(ModRequestDTO modRegister, UUID serverId, UUID userId) {
		var server = serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		if(modRepository.existsInServer(serverId, modRegister.getActivationId()))
			throw new BusinessException("Mod's already in server modlist");
		
		var mod = buildMod(modRegister);
		mod.setServer(server);
		return ModResponseDTO.from(modRepository.save(mod));
	}
	
	public Slice<ModResponseDTO> findServerMods(UUID serverId, UUID userId){
		var server = serverRepository.findByIdAndUserId(serverId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Server not found"));
		
		return modRepository.findServerMods(server.getId())
				.map(ModResponseDTO::from);
	}
	
	public ServerMod buildMod(ModRequestDTO modRegister) {
		return ServerMod.builder()
		.activationId(modRegister.getActivationId())
		.modName(modRegister.getModName())
		.isActive(true)
		.build();
	}
}
