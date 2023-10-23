package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String PATH = "/users";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpTest() {
        UserController userController = new UserController();
    }

    @Test
    public void positiveGetAllEmptyShouldReturnCode200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void positivePostShouldReturnCode200() throws Exception {
        User user = new User("friend@common.ru", "test_t", "TestUsername", LocalDate.of(2011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()));
    }

    @Test
    public void positivePostWithEmptyNameShouldReturnCode200() throws Exception {
        User user = new User("friend@common.ru", "test_t", null, LocalDate.of(2011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getLogin()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()));
    }

    @Test
    public void negativePostWithNoCorrectEmailShouldReturn400() throws Exception {
        User user = new User("test12312 com", "test_t", "TestUsername", LocalDate.of(2011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        User user2 = new User(null, "test_t", "TestUsername", LocalDate.of(2011, 11, 11));
        String userJson2 = objectMapper.writeValueAsString(user2);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        User user3 = new User("                         ", "test_t", "TestUsername", LocalDate.of(2011, 11, 11));
        String userJson3 = objectMapper.writeValueAsString(user3);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectLoginShouldReturn400() throws Exception {
        User user = new User("test@test.com", "test login", "TestUsername", LocalDate.of(2011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectBirthDayShouldReturn400() throws Exception {
        User user = new User("test@test.com", "testlogin", "TestUsername", LocalDate.of(3011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveUpdateShoudReturnCode200() throws Exception {
        User user = new User("test@test.com", "test_t", "TestUsername", LocalDate.of(2011, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        user.setId(parsedId.longValue());
        user.setName("TestUsername1");
        user.setBirthday(LocalDate.of(2002, 11, 11));

        String userJson2 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(user.getId()))
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()));
    }

    @Test
    public void positiveUpdateWithEmptyNameShoudReturnCode200() throws Exception {
        User user = new User("test@test.com", "test_t", null, LocalDate.of(2001, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getLogin()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        user.setId(parsedId.longValue());
        user.setName("TestUsername1");

        String userJson2 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(user.getId()))
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()));
    }

    @Test
    public void negativeUpdateWithNoEmptyLoginShoudReturnCode200() throws Exception {
        User user = new User("test@test.com", "test_t", "TestUsername1", LocalDate.of(2001, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        user.setId(parsedId.longValue());
        user.setLogin(null);

        String userJson2 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectBirthdayShoudReturnCode200() throws Exception {
        User user = new User("test@test.com", "test_t", "TestUsername1", LocalDate.of(2001, 11, 11));
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("email").value(user.getEmail()))
                .andExpect(jsonPath("login").value(user.getLogin()))
                .andExpect(jsonPath("name").value(user.getName()))
                .andExpect(jsonPath("birthday").value(user.getBirthday().toString()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        user.setId(parsedId.longValue());
        user.setBirthday(LocalDate.of(3001, 1, 1));

        String userJson2 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateNotInRepositoryShoutThrowNestedServletException() throws Exception {
        User user = new User("test@test.com", "test_t", "TestUsername1", LocalDate.of(2001, 11, 11));

        user.setId(9999L);

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1)));
    }

    @Test
    public void negativeUpdateNullIdShouldThrowValidationException() throws Exception {
        User user = new User("test@test.com", "test_t", "TestUsername1", LocalDate.of(2001, 11, 11));

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson1 = objectMapper.writeValueAsString(user);

        assertThrows(NestedServletException.class, () -> mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson1)));
    }

}