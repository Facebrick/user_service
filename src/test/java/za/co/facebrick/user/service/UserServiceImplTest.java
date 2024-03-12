package za.co.facebrick.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.data.model.User;
import za.co.facebrick.user.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    List<UserDto> userList;

    UserDto userDto1;

    UserDto userDto2;

    UserDto invalidUser;

    User user1;

    User user2;

    List<User> dataUserList;

    @BeforeEach
    public void setupEach() {
        userDto1 = UserDto.builder()
                .id(1L)
                .firstName("Name1")
                .lastName("Lastname1")
                .email("email1@email.com")
                .build();

        userDto2 = UserDto.builder()
                .id(2L)
                .firstName("Name2")
                .lastName("Lastname2")
                .email("email2@email.com")
                .build();

        invalidUser = UserDto.builder()
                .id(3L)
                .firstName("")
                .lastName("")
                .email("")
                .build();

        userList = List.of(userDto1, userDto2);

        user1 = User.builder()
                .id(1L)
                .firstName("Name1")
                .lastName("Lastname1")
                .email("email1@email.com")
                .build();
        user2 = User.builder()
                .id(2L)
                .firstName("Name2")
                .lastName("Lastname2")
                .email("email2@email.com")
                .build();

        dataUserList = List.of(user1, user2);
    }


    @Test
    void givenUsers_whenGetUsers_thenReturnListUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(dataUserList);

        Optional<List<UserDto>> response = userService.getUsers();

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get().size()).isEqualTo(userList.size());
        Assertions.assertThat(response.get().get(0).getId()).isEqualTo(userDto1.getId());
        Assertions.assertThat(response.get().get(0).getFirstName()).isEqualTo(userDto1.getFirstName());
        Assertions.assertThat(response.get().get(0).getLastName()).isEqualTo(userDto1.getLastName());
        Assertions.assertThat(response.get().get(0).getEmail()).isEqualTo(userDto1.getEmail());

        Assertions.assertThat(response.get().get(1).getId()).isEqualTo(userDto2.getId());
        Assertions.assertThat(response.get().get(1).getFirstName()).isEqualTo(userDto2.getFirstName());
        Assertions.assertThat(response.get().get(1).getLastName()).isEqualTo(userDto2.getLastName());
        Assertions.assertThat(response.get().get(1).getEmail()).isEqualTo(userDto2.getEmail());
    }

    @Test
    void givenNoUsers_whenGetUsers_thenReturnEmpty() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        Optional<List<UserDto>> response = userService.getUsers();

        Assertions.assertThat(response.isPresent()).isFalse();

    }

    @Test
    void givenValidId_whenGetUser_thenReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Optional<UserDto> response = userService.getUser(1L);

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get().getId()).isEqualTo(userDto1.getId());
        Assertions.assertThat(response.get().getFirstName()).isEqualTo(userDto1.getFirstName());
        Assertions.assertThat(response.get().getLastName()).isEqualTo(userDto1.getLastName());
        Assertions.assertThat(response.get().getEmail()).isEqualTo(userDto1.getEmail());
    }

    @Test
    void givenUnknownId_whenGetUser_thenReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> response = userService.getUser(1L);

        Assertions.assertThat(response.isPresent()).isFalse();
    }


    @Test
    void givenUserExists_whenUpdateUser_thenUpdateAndReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Optional<UserDto> response = userService.updateUser(userDto1);

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get().getId()).isEqualTo(userDto1.getId());
        Assertions.assertThat(response.get().getFirstName()).isEqualTo(userDto1.getFirstName());
        Assertions.assertThat(response.get().getLastName()).isEqualTo(userDto1.getLastName());
        Assertions.assertThat(response.get().getEmail()).isEqualTo(userDto1.getEmail());
    }

    @Test
    void givenUserDoesNotExist_whenUpdateUser_thenReturnEmptyUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> response = userService.updateUser(userDto1);

        Assertions.assertThat(response.isPresent()).isFalse();
    }

    @Test
    void givenInvalidUser_whenUpdateUser_thenThrowException() {

        Assertions.assertThatThrownBy(() -> {
            Optional<UserDto> response = userService.updateUser(invalidUser);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenValidUserDoesNotExists_whenCreateUser_thenUpdateAndReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> response = userService.createUser(userDto1);

        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get().getId()).isEqualTo(userDto1.getId());
        Assertions.assertThat(response.get().getFirstName()).isEqualTo(userDto1.getFirstName());
        Assertions.assertThat(response.get().getLastName()).isEqualTo(userDto1.getLastName());
        Assertions.assertThat(response.get().getEmail()).isEqualTo(userDto1.getEmail());
    }

    @Test
    void givenUserExist_whenCreateUser_thenReturnEmptyUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Optional<UserDto> response = userService.createUser(userDto1);

        Assertions.assertThat(response.isPresent()).isFalse();
    }

    @Test
    void givenInvalidUser_whenCreateUser_thenThrowException() {

        Assertions.assertThatThrownBy(() -> {
            Optional<UserDto> response = userService.createUser(invalidUser);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void givenValidIdExists_whenDeleteUser_thenDeleteAndReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Optional<UserDto> response = userService.deleteUser(1L);

        Mockito.verify(userRepository).deleteById(1L);
        Assertions.assertThat(response.isPresent()).isTrue();
        Assertions.assertThat(response.get().getId()).isEqualTo(userDto1.getId());
        Assertions.assertThat(response.get().getFirstName()).isEqualTo(userDto1.getFirstName());
        Assertions.assertThat(response.get().getLastName()).isEqualTo(userDto1.getLastName());
        Assertions.assertThat(response.get().getEmail()).isEqualTo(userDto1.getEmail());

    }
    @Test
    void givenValidIdDoesNotExist_whenDeleteUser_thenDeleteAndReturnUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> response = userService.deleteUser(1L);

        Assertions.assertThat(response.isPresent()).isFalse();

    }
}