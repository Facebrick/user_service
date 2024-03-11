package za.co.facebrick.user.service;

import za.co.facebrick.user.controller.model.RestUser;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<List<RestUser>> getUsers();

    Optional<RestUser> getUser(Long id);

    Optional<RestUser> createUser(RestUser user);

    Optional<RestUser> deleteUser(Long id);



}
