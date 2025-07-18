package com.orquideastour.microservice_users.service;


import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.Position;

import java.util.List;
import java.util.Optional;

public interface IUserService
{
          List<User> findAll();
          Optional<User> findById(Long id);
          Optional<User> findByEmail(String email);
          User save(User user);
          Optional<User> update(User user, Long id);
          void deleteById(Long id);
          Optional<User> findByUsername(String username);
          Long countUsers();
          Long countUsersActive();
          Double getTotalSalario();
          List<User> findByPuestoNotClient();
          List<User> finByPuestoClient();
          List<User> ListAllChoferes();

}
