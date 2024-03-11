package za.co.facebrick.user.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.facebrick.user.controller.model.RestUser;
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
    public List<RestUser> getUsers() {
        return null;
    }

    @GetMapping("/users")
    public RestUser getUserById(@RequestParam long id) {
        return null;
    }

    @PostMapping("/users")
    public ResponseEntity<RestUser> createUser(@RequestBody RestUser newUser) {
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users")
    public ResponseEntity<RestUser> updateUser(@RequestParam long id) {
        RestUser user = null;
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users")
    public ResponseEntity<RestUser> deleteUser(@RequestParam long id) {
        RestUser user = null;
        return ResponseEntity.ok(user);
    }



}
