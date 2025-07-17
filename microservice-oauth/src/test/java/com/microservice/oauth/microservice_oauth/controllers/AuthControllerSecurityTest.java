package com.microservice.oauth.microservice_oauth.controllers;

import com.microservice.oauth.microservice_oauth.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(AuthController.class)
class AuthControllerSecurityTest {

    private WebTestClient webTestClient;

    @MockBean
    private ReactiveAuthenticationManager authenticationManager;

    @MockBean
    private JwtEncoder jwtEncoder;

    private final String VALID_TOKEN = "valid.jwt.token";

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(
                new AuthController(authenticationManager, jwtEncoder)
        ).configureClient().baseUrl("/").build();
    }

    @Test
    void loginWithValidCredentials_ShouldReturnToken() {
        // Arrange
        User loginUser = new User();
        loginUser.setUsername("admin");
        loginUser.setPassword("123456");

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginUser.getUsername(),
                loginUser.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(3600);

        Jwt jwt = new Jwt(
                VALID_TOKEN,
                issuedAt,
                expiresAt,
                Map.of("alg", "RS256"), // Headers obligatorios
                Map.of("sub", loginUser.getUsername(), "roles", List.of("ROLE_USER")) // Claims válidos
        );

        // Mocks
        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(Mono.just(auth));

        Mockito.when(jwtEncoder.encode(any(JwtEncoderParameters.class)))
                .thenReturn(jwt);

        // Act & Assert
        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.access_token").isEqualTo(VALID_TOKEN);
    }

    @Test
    void loginWithInvalidPassword_ShouldReturnUnauthorized() {
        User loginUser = new User();
        loginUser.setUsername("admin");
        loginUser.setPassword("wrongpass");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(Mono.error(new BadCredentialsException("Contraseña incorrecta")));

        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginUser)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Contraseña incorrecta");
    }

    @Test
    void loginWithNonExistentUser_ShouldReturnUnauthorized() {
        User loginUser = new User();
        loginUser.setUsername("nonexistent");
        loginUser.setPassword("123456");

        Mockito.when(authenticationManager.authenticate(any()))
                .thenReturn(Mono.error(new UsernameNotFoundException("Email incorrecto")));

        webTestClient.post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginUser)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Email incorrecto");
    }
}
