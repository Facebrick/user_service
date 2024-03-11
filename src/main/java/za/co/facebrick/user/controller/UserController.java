package za.co.facebrick.user.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.service.UserService;

import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger();

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return null;
    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@RequestParam long id) {
        return null;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto newUser) {
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestParam long id) {
        UserDto user = null;
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@RequestParam long id) {
        UserDto user = null;
        return ResponseEntity.ok(user);
    }



}
