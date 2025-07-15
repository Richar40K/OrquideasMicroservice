package com.orquideastour.microservice_users.service;

import com.orquideastour.microservice_users.entities.Role;
import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.enums.Position;
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
import java.util.stream.Collectors;

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
        if(user.getUsername() == null || user.getUsername().isBlank()) {
            String dni = user.getDni();
            user.setUsername(dni);
            user.setPassword(passwordEncoder.encode(dni));
        } else if(user.getPassword()==null || user.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(user.getUsername()));
        else
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getEstado() ==null)
            user.setEstado(State.ACTIVO);
        if(user.getPuesto()==null)
            user.setPuesto(Position.CLIENTE);
        user.setEnabled(true);
        user.setRoles(getRoles(user));
        User persisted = userRepository.save(user);
        String codigo = persisted.generateCodigo(persisted.getId());
        persisted.setCodigo(codigo);
        return userRepository.save(persisted);
    }

    @Override
    @Transactional
    public Optional<User> update(User user, Long id) {
        return userRepository.findById(id)
                .map(usDb -> {
                    usDb.setName(user.getName());
                    usDb.setSecondName(user.getSecondName());
                    usDb.setLastName(user.getLastName());
                    usDb.setEmail(user.getEmail());
                    usDb.setCellPhone(user.getCellPhone());
                    if (StringUtils.isNotBlank(user.getPassword())) {
                        usDb.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    usDb.setDepartamento(user.getDepartamento());
                    usDb.setSalario(user.getSalario());
                    usDb.setPuesto(user.getPuesto());
                    usDb.setEstado(user.getEstado());

                    if (user.getAdmin() != null) {
                        usDb.setAdmin(user.getAdmin());
                    }
                    usDb.setEnabled(user.isEnabled() == null ? true : user.isEnabled());
                    usDb.setRoles(getRoles(usDb));
                    return userRepository.save(usDb);
                });
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
    @Transactional(readOnly = true)
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUsersActive() {
        return userRepository.countByEstado(State.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalSalario() {
        return userRepository.getTotalSalario();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByPuestoNotClient() {
        return (List<User>) userRepository.findByPuestoNot(Position.CLIENTE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> finByPuestoClient() {
        return (List<User>) userRepository.findByPuesto(Position.CLIENTE);
    }

    private List<Role> getRoles(User  user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add);
        if(Boolean.TRUE.equals(user.isAdmin())) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public List<User> findByPuestoChofer() {
        return (List<User>) userRepository.findByPuesto(Position.CHOFER);
    }
}
