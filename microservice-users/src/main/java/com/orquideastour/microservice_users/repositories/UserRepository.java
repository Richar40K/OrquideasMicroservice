package com.orquideastour.microservice_users.repositories;


import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.State;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long>
{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String email);
    Long countByEstado(State estado);
}
