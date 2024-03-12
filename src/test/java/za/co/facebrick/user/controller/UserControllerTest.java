package za.co.facebrick.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
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

    UserDto userDto1;

    UserDto userDto2;

    UserDto invalidUser;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeTestClass
    public void setupEach() {
        userDto1 = new UserDto(1L, "Name1", "Lastname1", "email1@email.com");
        userDto2 = new UserDto(2L, "Name2", "Lastname2", "email2@email.com");
        invalidUser = new UserDto(3L, "", "", "");
        userList = List.of(userDto1, userDto2);
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnUserList() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(Optional.ofNullable(userList));

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userList)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userDto1.getId())))
                .andExpect(jsonPath("$[0].firstName", is(userDto1.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(userDto1.getLastName())))
                .andExpect(jsonPath("$[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(userDto2.getId())))
                .andExpect(jsonPath("$[1].firstName", is(userDto2.getFirstName())))
                .andExpect(jsonPath("$[1].lastName", is(userDto2.getLastName())))
                .andExpect(jsonPath("$[1].email", is(userDto2.getEmail())));

    }

    @Test
    public void givenNoUsers_whenGetUsers_thenReturnNoContent() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("")));

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void givenIdExists_whenGetUserById_thenReturnUser() throws Exception {
        Mockito.when(userService.getUser(1L)).thenReturn(Optional.ofNullable(userDto1));

        ResultActions response = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.firstName", is(userDto1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto1.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    public void givenIdNotExists_whenGetUserById_thenReturnNoContent() throws Exception {
        Mockito.when(userService.getUser(3L)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(get("/users/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    void givenValidUser_whenCreateUser_thenReturnUserOk() throws Exception {
        Mockito.when(userService.createUser(userDto1)).thenReturn(Optional.ofNullable(userDto1));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto1)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.firstName", is(userDto1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto1.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void givenValidUserButAlreadyExists_whenCreateUser_thenReturnUserOk() throws Exception {
        Mockito.when(userService.createUser(userDto1)).thenReturn(Optional.ofNullable(userDto1));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto1)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user already exists")));
    }

    @Test
    void givenInvalidUser_whenCreateUser_thenReturnBadRequest() throws Exception {
        Mockito.when(userService.createUser(invalidUser)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user is invalid")));
    }


    @Test
    void givenNullUser_whenCreateUser_thenReturnBadRequest() throws Exception {
        Mockito.when(userService.createUser(null)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user is invalid")));
    }


    @Test
    void givenValidUser_whenUpdateUser_thenReturnUser() throws Exception {
        Mockito.when(userService.updateUser(userDto1)).thenReturn(Optional.ofNullable(userDto1));

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto1)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.firstName", is(userDto1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto1.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void givenUserDoesNotExist_whenUpdateUser_thenReturnUser() throws Exception {
        Mockito.when(userService.updateUser(userDto1)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto1)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user does not exist")));
    }

    @Test
    void givenInvalidUser_whenUpdateUser_thenReturnBadRequest() throws Exception {
        Mockito.when(userService.updateUser(invalidUser)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user is invalid")));
    }


    @Test
    void givenNullUser_whenUpdateUser_thenReturnBadRequest() throws Exception {
        Mockito.when(userService.updateUser(null)).thenThrow(IllegalArgumentException.class);

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Provided user is invalid")));
    }

    @Test
    void givenIdExists_whenDeleteUser_thenReturnUser() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenReturn(Optional.ofNullable(userDto1));

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId())))
                .andExpect(jsonPath("$.firstName", is(userDto1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userDto1.getLastName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void givenIdDoesNotExist_whenDeleteUser_thenReturnNotFound() throws Exception {
        Mockito.when(userService.deleteUser(1L)).thenReturn(Optional.empty());

        ResultActions response = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""));

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

}