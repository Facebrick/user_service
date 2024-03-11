package za.co.facebrick.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import za.co.facebrick.user.controller.model.UserDto;
import za.co.facebrick.user.data.model.User;
import za.co.facebrick.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;

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

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setupEach() {
        userDto1 = new UserDto(1L, "Name1", "Lastname1", "email1@email.com");
        userDto2 = new UserDto(2L, "Name2", "Lastname2", "email2@email.com");

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
                .content(objectMapper.writeValueAsString(userList)));

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test public void givenId_whenGetUserById_thenReturnUser() throws Exception {

    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

}