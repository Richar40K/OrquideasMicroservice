package com.orquideastour.microservice_users.service;

import com.orquideastour.microservice_users.entities.Role;
import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.State;
import com.orquideastour.microservice_users.repositories.RoleRepository;
import com.orquideastour.microservice_users.repositories.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setRoles(getRoles(user));
        User persisted = userRepository.save(user);
        String codigo = persisted.generateCodigo(persisted.getId());
        persisted.setCodigo(Long.valueOf(codigo));
        return userRepository.save(persisted);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {
        Optional<User> userOptional = this.findById(id);
        return  userOptional.map( usdb -> {
            usdb.setName(user.getName());
            usdb.setSecondName(user.getSecondName());
            usdb.setLastName(user.getLastName());
            usdb.setEmail(user.getEmail());
            usdb.setCellPhone(user.getCellPhone());
            if (StringUtils.isNotBlank(user.getPassword())) {
                usdb.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            usdb.setDepartamento(user.getDepartamento());
            usdb.setSalario(user.getSalario());
            usdb.setPuesto(user.getPuesto());
            usdb.setEstado(user.getEstado());
            if(user.getAdmin()!=null)
                usdb.setAdmin(user.getAdmin());
            if(user.isEnabled()==null)
                usdb.setEnabled(true);
            else
                usdb.setEnabled(user.isEnabled());
            return Optional.of(userRepository.save(usdb));
        }).orElseGet(()->Optional.empty());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
    userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional
    public Long countUsersActive() {
        return userRepository.countByEstado(State.ACTIVO);
    }

    private List<Role> getRoles(User  user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add); //otra forma
        if(Boolean.TRUE.equals(user.isAdmin())) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        }
        return roles;
    }
}
