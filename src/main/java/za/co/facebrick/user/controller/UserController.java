package za.co.facebrick.user.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private static final Logger LOG = LogManager.getLogger();

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        LOG.info("getUsers()");
        try {
            Optional<List<UserDto>> users = userService.getUsers();
            if (users.isPresent()) {
                LOG.debug("Returning users {}", users);
                return users.get();
            } else {
                LOG.debug("No users to return");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }

        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return null;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto newUser) {
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        UserDto user = null;
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        UserDto user = null;
        return ResponseEntity.ok(user);
    }


}
