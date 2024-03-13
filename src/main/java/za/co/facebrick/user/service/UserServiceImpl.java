package za.co.facebrick.user.service;

import jakarta.validation.ConstraintViolation;
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

    /**
     * This method fetches all users from the database
     * @return Optional Present List of UserDto on success. Optional Empty List when no users.
     * @throws DataAccessException when database is down or errored.
     */
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
        } catch (DataAccessException e) {
            LOG.error("An exception has occured", e);
            throw e;
        }
    }

    /**
     * getUser method returns a single UserDto for a specified Long id.
     * @param id The id of the user
     * @return Optional Present UserDto on success. Optional Empty UserDto when no user found.
     * @throws DataAccessException when database is down or errored.
     */
    @Override
    public Optional<UserDto> getUser(Long id) throws DataAccessException {
        LOG.debug("getUser({})", id);
        Optional<User> user = userRepository.findById(id);
        LOG.debug("User with id {} found: {}", id, user.isPresent());
        return user.map(this::convertUser);
    }

    /**
     * createUser method returns a single UserDto and creates a nonexistent user.
     * @param user User to create
     * @return Optional Present UserDto on success. Optional Empty UserDto when unable to add.
     * @throws DataAccessException when database is down or errored.
     * @throws IllegalArgumentException when input is invalid
     */
    @Override
    public Optional<UserDto> createUser(UserDto user) throws DataAccessException, IllegalArgumentException {
        LOG.debug("createUser({})", user);
        validateUser(user);
        User savedUser = userRepository.save(convertUserDto(user));
        if (savedUser.getId() != null) {
            return Optional.of(convertUser(savedUser));
        } else {
            return Optional.empty();
        }
    }

    /**
     * updateUser method updates a user if it exists within the database
     * @param id the id of the user to update
     * @param updatedUser the update details of the user
     * @return Optional Present UserDto on success. Optional Empty UserDto when user doesn't exist.
     * @throws DataAccessException when database is down or errored.
     * @throws IllegalArgumentException when updatedUser is invalid
     */
    @Override
    public Optional<UserDto> updateUser(Long id, UserDto updatedUser) throws DataAccessException, IllegalArgumentException {
        LOG.debug("updateUser({})", updatedUser);
        validateUser(updatedUser);
        Optional<User> userExists = userRepository.findById(id);
        if (userExists.isPresent()) {
            updateUser(userExists.get(), updatedUser);
            userRepository.save(userExists.get());
            return Optional.of(convertUser(userExists.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * deleteUser method deletes a user for the given ID if exists
     * @param id user id to delete
     * @return Optional Present UserDto on success. Optional Empty UserDto when user doesn't exist.
     * @throws DataAccessException when database is down or errored.
     */
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

    /**
     * Convert a Data Entity to a http dto
     * @param user data entity to convert
     * @return http dto UserDto
     */
    protected UserDto convertUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Convert a http dto to Data Entity
     * @param user http dto to convert
     * @return Data Entity
     */
    protected User convertUserDto(UserDto user) {
        return User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    /**
     * Used to copy the details of the updated user to the existing
     * @param currentUser
     * @param updatedUser
     */
    protected void updateUser(User currentUser, UserDto updatedUser) {
        currentUser.setFirstName(updatedUser.getFirstName());
        currentUser.setLastName(updatedUser.getLastName());
        currentUser.setEmail(updatedUser.getEmail());
    }

    /**
     * This method validates inputed UserDto objects
     * @param user User to validate
     * @throws IllegalArgumentException When UserDto Failes validation
     */
    protected void validateUser(UserDto user) throws IllegalArgumentException {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UserDto> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage() + "\n");
            }
            throw new IllegalArgumentException("Provided input was invalid: \n" + violations);
        }
    }
}
