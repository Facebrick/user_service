package za.co.facebrick.user.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import za.co.facebrick.user.controller.model.UserDto;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    Optional<List<UserDto>> getUsers() throws DataAccessException;

    Optional<UserDto> getUser(Long id) throws DataAccessException;

    Optional<UserDto> createUser(UserDto user) throws DataAccessException, IllegalArgumentException;

    Optional<UserDto> updateUser(Long id, UserDto user) throws DataAccessException, IllegalArgumentException;

    Optional<UserDto> deleteUser(Long id) throws DataAccessException;

}
