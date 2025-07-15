package com.orquideastour.microservice_users.repositories;


import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.Position;
import com.orquideastour.microservice_users.enums.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long>
{
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Long countByEstado(State estado);
    @Query("SELECT COALESCE(SUM(u.salario), 0) FROM User u")
    Double getTotalSalario();

    List<User> findByPuesto(Position puesto);
    List<User> findByPuestoNot(Position puesto);
    List<User> findByPuestoChofer(Position puesto, State estado);
}
