package com.dashboard.api;

import com.dashboard.api.dto.ServerRequestDTO;
import com.dashboard.api.dto.ServerResponseDTO;
import com.dashboard.api.entity.Role;
import com.dashboard.api.entity.Server;
import com.dashboard.api.entity.Status;
import com.dashboard.api.entity.User;
import com.dashboard.api.exception.BusinessException;
import com.dashboard.api.exception.ResourceNotFoundException;
import com.dashboard.api.repository.ServerRepository;
import com.dashboard.api.repository.UserRepository;
import com.dashboard.api.service.ServerService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServerServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

    private static final UUID userId = UUID.randomUUID();
    private static final UUID serverId = UUID.randomUUID();
    private User user;
    private Server server;
    private ServerRequestDTO serverRequestDTO;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .id(userId)
                .email("andrei@gmail.com")
                .name("andrei")
                .password("1234567")
                .role(Role.ADMIN)
                .servers(new HashSet<>())
                .build();

        server = Server.builder()
                .id(serverId)
                .port("8080")
                .ip("213123123")
                .name("server test")
                .gameName("test game")
                .status(Status.OFFLINE)
                .users(new HashSet<>(Set.of(user)))
                .build();

        user.getServers().add(server);

        serverRequestDTO = ServerRequestDTO.builder()
                .port("8080")
                .ip("213123123")
                .name("server test")
                .gameName("test game")
                .build();

    }

    @Test
    void createServer_withValidUser(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serverRepository.existsByPortAndUserId(serverRequestDTO.getPort(), user.getId())).thenReturn(false);
        when(serverRepository.save(any())).thenReturn(server);

        ServerResponseDTO serverResponseDTO = serverService.create(serverRequestDTO, userId);

        verify(serverRepository, times(1)).save(any());
        assertNotNull(serverResponseDTO);
        assertThat(user.getServers().equals(Set.of(server)));
        assertEquals(1, user.getServers().size());
    }

    @Test
    void createServer_withInvalidUser(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> serverService.create(serverRequestDTO,userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(serverRepository, never()).save(any());
    }

    @Test
    void createServer_withInvalidPort(){
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serverRepository.existsByPortAndUserId(serverRequestDTO.getPort(), userId)).thenReturn(true);

        assertThatThrownBy(() -> serverService.create(serverRequestDTO, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Server with that port already exists");
        verify(serverRepository, never()).save(any());
    }

    @Test
    void createServer_withInvalidRole(){
        user.setRole(Role.MODERATOR);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serverRepository.existsByPortAndUserId(serverRequestDTO.getPort(), userId)).thenReturn(false);

        assertThatThrownBy(() -> serverService.create(serverRequestDTO, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Only the admin can create new servers");
        verify(serverRepository, never()).save(any());
    }

    @Test
    void findAll_userServersWithSearchTerm(){

        Pageable pageable = PageRequest.of(0, 10);

        var secondServer = Server.builder()
                .id(serverId)
                .port("8080")
                .ip("213123123")
                .name("servers")
                .gameName("test game")
                .users(new HashSet<>(Set.of(user)))
                .build();

        user.getServers().add(secondServer);

        List<Server> servers = List.of(server, secondServer);

        Slice<Server> slice = new SliceImpl<>(servers, pageable, false);

        when(serverRepository.findAllBySearchTerm("server", user.getId(), pageable)).thenReturn(slice);

        Slice<ServerResponseDTO> serverResponse = serverService.findUserServerBySearchTerm("server", user.getId(), pageable);

        assertThat(serverResponse.getContent())
                .hasSize(2)
                .extracting(ServerResponseDTO::getName)
                .allMatch(name -> name.contains("server"));

        verify(serverRepository, times(1)).findAllBySearchTerm(any(), any(), any());

    }

    @Test
    void findAll_servers(){
        Pageable pageable = PageRequest.of(0, 10);

        var secondUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .name("test")
                .password("1234567")
                .role(Role.ADMIN)
                .servers(new HashSet<>())
                .build();

        var secondServer = Server.builder()
                .id(serverId)
                .port("8080")
                .ip("213123123")
                .name("servers")
                .gameName("test game")
                .users(new HashSet<>(Set.of(secondUser)))
                .build();

        user.getServers().add(secondServer);

        List<Server> serversList = List.of(server, secondServer);
        Slice<Server> servers = new SliceImpl<>(serversList, pageable, false);

        when(serverRepository.findAllBySearchTerm("server", pageable)).thenReturn(servers);
        Slice<ServerResponseDTO> serverResponse = serverService.findServerBySearchTerm("server", pageable);

        assertThat(serverResponse.getContent())
                .hasSize(2)
                .extracting(ServerResponseDTO::getName)
                .allMatch(name -> name.contains("server"));
        verify(serverRepository, times(1)).findAllBySearchTerm(any(), any());
    }

    @Test
    void findServer_withIdAndUserIdValid(){
        when(serverRepository.findByIdAndUserId(server.getId(), user.getId())).thenReturn(Optional.of(server));
        ServerResponseDTO serverResponse = serverService.findByIdAndUserId(server.getId(), user.getId());
        assertNotNull(serverResponse);
        assertThat(serverResponse.getId().equals(server.getId()));
        verify(serverRepository, times(1)).findByIdAndUserId(any(), any());
    }

    @Test
    void findServer_withIdOrUserIdInvalid(){
        assertThatThrownBy(() -> serverService.findByIdAndUserId(null, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Server not found");
    }

    @Test
    void toggleServer_withIdAndUserIdValid(){
        when(serverRepository.findByIdAndUserId(server.getId(), user.getId())).thenReturn(Optional.of(server));
        ServerResponseDTO serverResponse = serverService.toggle(server.getId(), user.getId());
        verify(serverRepository, times(1)).findByIdAndUserId(any(), any());
        assertThat(serverResponse.getStatus() == Status.ONLINE);
    }

    @Test
    void toggleServer_withIdOrUserInvalid(){
        assertThatThrownBy(() -> serverService.toggle(null, null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Server not found");
    }


}
