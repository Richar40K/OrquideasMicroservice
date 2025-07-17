package com.orquideastour.microservice_users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = new User();
        user.setName("Carlos");
        user.setDni("11112222");
        user.setEmail("carlos@example.com");
        user.setPassword("123456");

        userRepository.save(user);
    }


    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Carlos")));
    }

    @Test
    void testGetUserById() throws Exception {
        Long userId = user.getId();

        mockMvc.perform(get("/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("carlos@example.com")));
    }

    @Test
    void testCountUsers() throws Exception {
        mockMvc.perform(get("/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setName("Lucía");
        newUser.setDni("99998888");
        newUser.setEmail("lucia@example.com");

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Lucía")))
                .andExpect(jsonPath("$.dni", is("99998888")));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        Long id = user.getId();

        mockMvc.perform(delete("/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/" + id))
                .andExpect(status().isNotFound());
    }
}
