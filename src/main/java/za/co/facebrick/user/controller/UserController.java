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
import za.co.facebrick.user.controller.model.ErrorResponse;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(produces = "application/json")
public class UserController {

    private static final Logger LOG = LogManager.getLogger();

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        LOG.info("getUsers()");
        try {
            Optional<List<UserDto>> users = userService.getUsers();
            if (users.isPresent()) {
                LOG.info("Returning users {}", users.get());
                return ResponseEntity.ok(users.get());
            } else {
                LOG.info("No users to return");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }

        } catch (DataAccessException e) {
            LOG.error("Data access exception occurred", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        LOG.info("getUserById({})", id);
        try {
            Optional<UserDto> user = userService.getUser(id);
            if (user.isPresent()) {
                LOG.info("Returning user {}", user.get());
                return ResponseEntity.ok(user.get());
            } else {
                LOG.info("No users to return");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }
        } catch (DataAccessException e) {
            LOG.error("Data access exception occurred", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto newUser) {
        LOG.info("createUser{}", newUser);
        try {
            Optional<UserDto> user = userService.createUser(newUser);

            if (user.isPresent()) {
                LOG.info("User was created {}", user.get());
                return new ResponseEntity<>(user.get(), HttpStatus.CREATED);
            } else {
                LOG.info("Unable to save user {}", newUser);
                return new ResponseEntity<>(new ErrorResponse("Unable to save user: " + newUser,
                        null),
                        HttpStatus.SERVICE_UNAVAILABLE);
            }
        } catch (DataAccessException e) {
            LOG.error("Data access exception occurred", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping(value = "/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        LOG.info("updateUser({}, {})", id, userDto);
        try {
            Optional<UserDto> user = userService.updateUser(id, userDto);

            if (user.isPresent()) {
                LOG.info("User was updated {}", user.get());
                return new ResponseEntity<>(user.get(), HttpStatus.CREATED);
            } else {
                LOG.info("Unable to save user {}", userDto);
                return new ResponseEntity<>(new ErrorResponse("Unable to update user. User does not exist:  " + userDto,
                        null),
                        HttpStatus.BAD_REQUEST);
            }

        } catch (DataAccessException e) {
            LOG.error("Data access exception occurred", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        LOG.info("deleteUser({})", id);
        try {
            Optional<UserDto> user = userService.deleteUser(id);

            if (user.isPresent()) {
                LOG.info("User has been deleted");
                return ResponseEntity.ok(user.get());
            } else {
                LOG.info("Unable to delete user with id {}, user doesn't exist", id);
                throw new ResponseStatusException((HttpStatus.NOT_FOUND));
            }
        } catch (DataAccessException e) {
            LOG.error("Data access exception occurred", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
