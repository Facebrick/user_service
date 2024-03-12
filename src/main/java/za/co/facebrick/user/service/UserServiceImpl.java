package za.co.facebrick.user.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.data.model.User;
import za.co.facebrick.user.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger();

    private UserRepository userRepository;

    private Validator validator;

    public UserServiceImpl(@Autowired UserRepository userRepository, @Autowired Validator validator) {
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public Optional<List<UserDto>> getUsers() throws DataAccessException {
        LOG.debug("getUsers()");
        try {
            List<User> users = userRepository.findAll();
            LOG.debug("Returned users: {}", users.toString());

            if (!users.isEmpty()) {
                List<UserDto> userDtos = new ArrayList<>();
                for (User user : users) {
                    userDtos.add(convertUser(user));
                }
                LOG.debug("Returning {} users to controller", userDtos.size());
                return Optional.of(userDtos);
            } else {
                return Optional.empty();
            }
        } catch(DataAccessException e) {
            LOG.error("An exception has occured", e);
            throw e;
        }
    }

    @Override
    public Optional<UserDto> getUser(Long id) throws DataAccessException {
        LOG.debug("getUser({})", id);
        Optional<User> user = userRepository.findById(id);
        LOG.debug("User with id {} found: {}", id, user.isPresent());
        return user.map(this::convertUser);
    }

    @Override
    public Optional<UserDto> createUser(@Valid UserDto user) throws DataAccessException, IllegalArgumentException {
        LOG.debug("createUser({})", user);
        validateUser(user);
        User savedUser = userRepository.save(convertUserDto(user));
        if (savedUser.getId() != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private void validateUser(UserDto user) throws IllegalArgumentException {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UserDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage() + "\n");
            }
            throw new IllegalArgumentException("Provided input was invalid: \n" + violations);
        }
    }

    @Override
    public Optional<UserDto> updateUser(Long id, @Valid UserDto user) throws DataAccessException, IllegalArgumentException {
        LOG.debug("updateUser({})", user);
        validateUser(user);
        if (userRepository.findById(id).isPresent()) {
            userRepository.save(convertUserDto(user));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> deleteUser(Long id) throws DataAccessException {
        LOG.debug("deleteUser({})", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return Optional.ofNullable(convertUser(user.get()));
        } else {
            return Optional.empty();
        }
    }

    protected UserDto convertUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    protected User convertUserDto(UserDto user) {
        return User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }
}
