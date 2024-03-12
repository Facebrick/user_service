package za.co.facebrick.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
@ExtendWith({MockitoExtension.class})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    List<UserDto> userList;

    UserDto finalUserDto;

    UserDto finalUserDto2;

    UserDto initialUserRequest;

    UserDto invalidUser;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setupEach() {
        finalUserDto = UserDto.builder()
                .id(1L)
                .firstName("Name1")
                .lastName("Lastname1")
                .email("email1@email.com")
                .build();

        finalUserDto2 = UserDto.builder()
                .id(2L)
                .firstName("Name2")
                .lastName("Lastname2")
                .email("email2@email.com")
                .build();

        initialUserRequest = UserDto.builder()
                .firstName("Name1")
                .lastName("Lastname1")
                .email("email1@email.com")
                .build();

        invalidUser = UserDto.builder()
                .firstName("")
                .lastName("")
                .email("")
                .build();

        userList = List.of(finalUserDto, finalUserDto2);
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnUserList() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(Optional.ofNullable(userList));

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userList)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(finalUserDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].firstName", is(finalUserDto.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(finalUserDto.getLastName())))
                .andExpect(jsonPath("$[0].email", is(finalUserDto.getEmail())))
                .andExpect(jsonPath("$[1].id", is(finalUserDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].firstName", is(finalUserDto2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(finalUserDto2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(finalUserDto2.getEmail())));

    }

    @Test
    public void givenNoUsers_whenGetUsers_thenReturnNoContent() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void givenUsersAndDatabaseFailure_whenGetUsers_thenReturnServiceUnavailable() throws Exception {
        Mockito.when(userService.getUsers()).thenThrow(new DataAccessException("Can't access database") {
        });

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }


    @Test
    public void givenIdExists_whenGetUserById_thenReturnUser() throws Exception {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(finalUserDto));

        ResultActions response = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(finalUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(finalUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(finalUserDto.getLastName())))
                .andExpect(jsonPath("$.email", is(finalUserDto.getEmail())));
    }

    @Test
    public void givenIdNotExists_whenGetUserById_thenReturnNoContent() throws Exception {
        Mockito.when(userService.getUser(3L)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void givenIdExistsAndDatabaseFailure_whenGetUserById_thenReturnServiceUnavailable() throws Exception {
        Mockito.when(userService.getUser(3L)).thenThrow(new DataAccessException("Can't access database") {
        });

        ResultActions response = mockMvc.perform(get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    void givenValidUser_whenCreateUser_thenReturnUserOk() throws Exception {
        Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(Optional.of(finalUserDto));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(finalUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(finalUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(finalUserDto.getLastName())))
                .andExpect(jsonPath("$.email", is(finalUserDto.getEmail())));
    }

    @Test
    void givenValidUserButAlreadyExists_whenCreateUser_thenServiceUnavailable() throws Exception {
        Mockito.when(userService.createUser(initialUserRequest)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable())
                .andExpect(jsonPath("$.failureReason", containsString("Unable to save user:"), String.class));
    }

    @Test
    void givenInvalidUser_whenCreateUser_thenReturnBadRequest() throws Exception {
//        Mockito.when(userService.createUser(invalidUser)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.failedInput", hasItems("Lastname cannot be blank or just whitespaces",
                        "Email cannot be blank or just whitespaces",
                        "Firstname cannot be blank or just whitespaces")));
    }

    @Test
    void givenValidUser_whenUpdateUser_thenReturnUser() throws Exception {
        Mockito.when(userService.updateUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.ofNullable(finalUserDto));

        ResultActions response = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(finalUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(finalUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(finalUserDto.getLastName())))
                .andExpect(jsonPath("$.email", is(finalUserDto.getEmail())));
    }

    @Test
    void givenUserDoesNotExist_whenUpdateUser_thenReturnUser() throws Exception {
        Mockito.when(userService.updateUser(1L, initialUserRequest)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.failureReason", containsString("Unable to update user. User does not exist: ")));
    }

    @Test
    void givenInvalidUser_whenUpdateUser_thenReturnBadRequest() throws Exception {
        Mockito.when(userService.updateUser(invalidUser.getId(), invalidUser)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.failedInput", hasItems("Lastname cannot be blank or just whitespaces",
                        "Email cannot be blank or just whitespaces",
                        "Firstname cannot be blank or just whitespaces")));
    }

    @Test
    public void givenUsersAndDatabaseFailure_whenUpdateUser_thenReturnServiceUnavailable() throws Exception {
        Mockito.when(userService.updateUser(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(new DataAccessException("Can't access database") {
        });

        ResultActions response = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(initialUserRequest)));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    void givenIdExists_whenDeleteUser_thenReturnUser() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenReturn(Optional.ofNullable(finalUserDto));

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(finalUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.firstName", is(finalUserDto.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(finalUserDto.getLastName())))
                .andExpect(jsonPath("$.email", is(finalUserDto.getEmail())));
    }

    @Test
    void givenIdDoesNotExist_whenDeleteUser_thenReturnNotFound() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenUsersAndDatabaseFailure_whenDeleteUser_thenReturnServiceUnavailable() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenThrow(new DataAccessException("Can't access database") {
        });

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

}