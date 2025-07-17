package com.orquideastour.microservice_users.service;

import com.orquideastour.microservice_users.entities.Role;
import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.Position;
import com.orquideastour.microservice_users.enums.State;
import com.orquideastour.microservice_users.repositories.RoleRepository;
import com.orquideastour.microservice_users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setDni("12345678");
        user.setName("Juan");
        user.setEmail("juan@example.com");
        user.setPuesto(null); // Lo probará el default: CLIENTE
        user.setEstado(null); // Lo probará el default: ACTIVO
        user.setAdmin(false);
    }

    @Test
    void testSave_WhenUsernameAndPasswordNull_ShouldSetDefaultsAndEncrypt() {
        Role role = new Role();
        role.setName("ROLE_USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        User savedUser = userService.save(user);

        assertEquals("12345678", savedUser.getUsername());
        assertEquals("encoded123", savedUser.getPassword());
        assertEquals(State.ACTIVO, savedUser.getEstado());
        assertEquals(Position.CLIENTE, savedUser.getPuesto());
        assertTrue(savedUser.isEnabled());
        assertEquals(1, savedUser.getRoles().size());
    }

    @Test
    void testFindById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> found = userService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Juan", found.get().getName());
    }

    @Test
    void testUpdate_ShouldModifyAndSaveUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPwd");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));

        User update = new User();
        update.setName("Carlos");
        update.setPassword("1234");
        update.setAdmin(true);

        Optional<User> updated = userService.update(update, 1L);

        assertTrue(updated.isPresent());
        assertEquals("Carlos", updated.get().getName());
        assertEquals("newEncodedPwd", updated.get().getPassword());
        assertTrue(updated.get().isAdmin());
    }

    @Test
    void testDeleteById() {
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCountUsers() {
        when(userRepository.count()).thenReturn(5L);
        assertEquals(5L, userService.countUsers());
    }

    @Test
    void testCountUsersActive() {
        when(userRepository.countByEstado(State.ACTIVO)).thenReturn(3L);
        assertEquals(3L, userService.countUsersActive());
    }

    @Test
    void testGetTotalSalario() {
        when(userRepository.getTotalSalario()).thenReturn(10000.0);
        assertEquals(10000.0, userService.getTotalSalario());
    }

    @Test
    void testFindByPuestoClient() {
        List<User> clientList = List.of(user);
        when(userRepository.findByPuesto(Position.CLIENTE)).thenReturn(clientList);
        assertEquals(1, userService.finByPuestoClient().size());
    }

    @Test
    void testFindByPuestoNotClient() {
        List<User> staff = List.of(user);
        when(userRepository.findByPuestoNot(Position.CLIENTE)).thenReturn(staff);
        assertEquals(1, userService.findByPuestoNotClient().size());
    }

    @Test
    void testListAllChoferes() {
        when(userRepository.findByPuesto(Position.CHOFER)).thenReturn(List.of(new User()));
        assertEquals(1, userService.ListAllChoferes().size());
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("juan")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByUsername("juan");
        assertTrue(result.isPresent());
        assertEquals("juan@example.com", result.get().getEmail());
    }
}
