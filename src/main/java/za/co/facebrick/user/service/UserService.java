package za.co.facebrick.user.service;

import za.co.facebrick.user.controller.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<List<UserDto>> getUsers();

    Optional<UserDto> getUser(Long id);

    Optional<UserDto> createUser(UserDto user);

    Optional<UserDto> deleteUser(Long id);



}
