package za.co.facebrick.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.data.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<List<UserDto>> getUsers() {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> getUser(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> createUser(UserDto user) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> deleteUser(Long id) {
        return Optional.empty();
    }
}
