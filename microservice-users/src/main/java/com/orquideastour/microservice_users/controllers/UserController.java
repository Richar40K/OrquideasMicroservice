package com.orquideastour.microservice_users.controllers;

import com.orquideastour.microservice_users.entities.User;
import com.orquideastour.microservice_users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class UserController
{
    @Autowired
    private IUserService userService;

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(userService.countUsers());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActive() {
        return ResponseEntity.ok(userService.countUsersActive());
    }
    @GetMapping("/salary/total")
    public ResponseEntity<Map<String, Double>> totalSalary() {
        Double sum = userService.getTotalSalario();
        return ResponseEntity.ok(Collections.singletonMap("total", sum));
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/empleados")
    public ResponseEntity<List<User>> getAllEmpleados(){ return ResponseEntity.ok(userService.findByPuestoNotClient());}

    @GetMapping("/clientes")
    public ResponseEntity<List<User>> getClients(){ return ResponseEntity.ok(userService.finByPuestoClient());}
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return userService.findById(id)
                .map(us -> ResponseEntity.ok(us)).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        return  userService.findByEmail(email)
                .map(us->ResponseEntity.ok(us)).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        return userService.findByUsername(username).
                map(us->ResponseEntity.ok(us)).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String username) {
        boolean exists = userService.findByUsername(username).isPresent();
        Map<String, Boolean> body = Collections.singletonMap("available", !exists);
        return ResponseEntity.ok(body);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable Long id){
        return  userService.update(user,id).
                map( userUpdate -> ResponseEntity.status(HttpStatus.CREATED).body(userUpdate))
                .orElseGet(()->ResponseEntity.noContent().build());

    }
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser =userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //test
    @PatchMapping("/saveadmin/{id}")
    public ResponseEntity<User> patchupdate(@RequestBody User user,@PathVariable Long id){
        return  userService.patchupdate(user,id).
                map( userUpdate -> ResponseEntity.status(HttpStatus.CREATED).body(userUpdate))
                .orElseGet(()->ResponseEntity.noContent().build());
    }
    //test

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/choferes")
    public  ResponseEntity <List<User>> getAllChoferes () {return ResponseEntity.ok(userService.ListAllChoferes());
    }


}
