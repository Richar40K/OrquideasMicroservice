package com.orquideastour.microservice_gateway.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/.well-known/jwks.json",
                                "/actuator/health"
                        ).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/api/users/{id}").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/count").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/count/active").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/salary/total").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/empleados").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/clientes").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/choferes").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/username/{username}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/check-username").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bus").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bus/activo").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/bus").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/api/bus/{id}").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/api/bus/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/bus/cantidad-activos").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/rutas").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/viajes").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/viajes/{id}").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/api/viajes/{id}").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/api/viajes/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/viajes/programados").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/viajes/{id}/asientos").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/viajes").permitAll()
                        .pathMatchers(HttpMethod.PUT, "/api/viajes/{viajeId}/asientos/{numero}/ocupar").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/viajes/rutas-populares").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/pagos/viaje").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/viaje").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/viajes/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/encomienda").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/pagos/encomienda").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/viaje/aprobados/{userId}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/encomienda/aprobadas/{userId}").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/total-aprobados").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/totales/encomiendas").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/totales/viajes").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/total-pendientes").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/total-aprobados-hoy").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/viajes-aprobados").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/encomiendas-aprobadas").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/encomiendas").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/encomiendas/code/{codigo}").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/encomiendas").permitAll()
                        .pathMatchers(HttpMethod.PATCH, "/api/encomiendas/{id}/estado").permitAll()
                        .pathMatchers(HttpMethod.DELETE, "/api/encomiendas/{id}").permitAll()
                        .pathMatchers(HttpMethod.GET, "api/users/check-username").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/pagos").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/pagos/sincronizar").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/encomienda").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/seguridad/login").permitAll()
                        .anyExchange().authenticated() ///rutas-populares
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(reactiveJwtDecoder())
                                .jwtAuthenticationConverter(customJwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));

        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        return NimbusReactiveJwtDecoder
                .withJwkSetUri("http://localhost:9100/.well-known/jwks.json")
                .webClient(webClient)
                .build();
    }

    private Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> customJwtAuthenticationConverter() {
        return jwt -> {
            Collection<String> roles = jwt.getClaimAsStringList("roles");
            Collection<GrantedAuthority> authorities = roles == null ?
                    java.util.Collections.emptyList() :
                    roles.stream()
                            .map(role -> {
                                if (!role.startsWith("ROLE_")) {
                                    return new SimpleGrantedAuthority("ROLE_" + role);
                                } else {
                                    return new SimpleGrantedAuthority(role);
                                }
                            })
                            .collect(Collectors.toList());
            return Mono.just(new JwtAuthenticationToken(jwt, authorities));
        };
    }
}