package za.co.facebrick.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.facebrick.user.controller.model.RestUser;
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
    public Optional<List<RestUser>> getUsers() {
        return Optional.empty();
    }

    @Override
    public Optional<RestUser> getUser(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<RestUser> createUser(RestUser user) {
        return Optional.empty();
    }

    @Override
    public Optional<RestUser> deleteUser(Long id) {
        return Optional.empty();
    }
}
