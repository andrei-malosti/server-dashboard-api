package com.dashboard.api.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.dashboard.api.dto.ModRequestDTO;
import com.dashboard.api.dto.ModResponseDTO;
import com.dashboard.api.entity.Mod;
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
		
		if(modRepository.existsByServerIdAndModStringId(serverId, modRegister.getModStringId()))
			throw new BusinessException("Mod's already in server modlist");
		
		var mod = buildMod(modRegister);
		mod.setServer(server);
		return ModResponseDTO.from(modRepository.save(mod));
	}
	
	public Slice<ModResponseDTO> findServerMods(UUID serverId, UUID userId, Pageable pageable){
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		return modRepository.findServerMods(serverId, true, pageable)
				.map(ModResponseDTO::from);
	}
	
	public Slice<ModResponseDTO> findServerInactiveMods(UUID serverId, UUID userId, Pageable pageable){
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		return modRepository.findServerMods(serverId, false, pageable)
				.map(ModResponseDTO::from);
	}
	
	@Transactional
	public ModResponseDTO activate(UUID serverId, UUID userId, String modStringId) {
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		var mod = modRepository.findModByModStringId(serverId, modStringId)
				.orElseThrow(() -> new ResourceNotFoundException("Mod not found in this server"));
		
		if(mod.getIsActive())
			throw new BusinessException(String.format("Mod with id %s is already active", mod.getModStringId()));
		
		mod.setIsActive(true);
		return ModResponseDTO.from(mod);
	}
	
	@Transactional
	public ModResponseDTO deactivate(UUID serverId, UUID userId, String modStringId) {
		if(!serverRepository.existsByIdAndUserId(serverId, userId))
			throw new ResourceNotFoundException("Server not found");
		
		var mod = modRepository.findModByModStringId(serverId, modStringId)
				.orElseThrow(() -> new ResourceNotFoundException("Mod not found in this server"));
		
		if(!mod.getIsActive())
			throw new BusinessException("Mod with id %s is already inactive");
		
		mod.setIsActive(false);
		return ModResponseDTO.from(mod);
	}
	
	public Mod buildMod(ModRequestDTO modRegister) {
		return Mod.builder()
		.modStringId(modRegister.getModStringId())
		.modName(modRegister.getModName())
		.isActive(true)
		.build();
	}
}
