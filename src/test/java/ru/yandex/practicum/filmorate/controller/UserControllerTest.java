package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;
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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final String PATH = "/users";
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpTest() {
        UserController userController;

        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Test
    public void positiveGetAllEmptyShouldReturnCode200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void positivePostShouldReturnCode200() throws Exception {
        User user = User.builder()
                .email("friend@common.ru")
                .login("test_t")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

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
        User user = User.builder()
                .email("friend@common.ru")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

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
        User user = User.builder()
                .email("test12312 com")
                .login("test_t")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        User user2 = User.builder()
                .login("test_t")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        String userJson2 = objectMapper.writeValueAsString(user2);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        User user3 = User.builder()
                .email("                         ")
                .login("test_t")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        String userJson3 = objectMapper.writeValueAsString(user3);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectLoginShouldReturn400() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test login")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectBirthDayShouldReturn400() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("testlogin")
                .name("TestUsername")
                .birthday(LocalDate.of(3011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveUpdateShoudReturnCode200() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .name("TestUsername")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

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
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

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
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .name("TestUsername1")
                .birthday(LocalDate.of(2001, 11, 11))
                .build();

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
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .name("TestUsername1")
                .birthday(LocalDate.of(2001, 11, 11))
                .build();

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
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .name("TestUsername1")
                .birthday(LocalDate.of(2001, 11, 11))
                .build();
        user.setId(9999L);

        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateNullIdShouldThrowValidationException() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .name("TestUsername1")
                .birthday(LocalDate.of(2001, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveGetByUserByIdShouldReturn200() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", parsedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void negativeGetByUserByIdShouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", -9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positivePutUsersFriends() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        user.setLogin("colacoca");

        String userJson2 = objectMapper.writeValueAsString(user);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();
        Integer parsedId1 = JsonPath.read(result1.getResponse().getContentAsString(), "id");
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andReturn();
        Integer parsedId2 = JsonPath.read(result2.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId1, parsedId2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId2, parsedId1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void negativePutUserFriends() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();
        Integer parsedId1 = JsonPath.read(result1.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId1, -9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveDeleteUsersFriends() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        user.setLogin("colacoca");

        String userJson2 = objectMapper.writeValueAsString(user);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();
        Integer parsedId1 = JsonPath.read(result1.getResponse().getContentAsString(), "id");
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andReturn();
        Integer parsedId2 = JsonPath.read(result2.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId1, parsedId2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId2, parsedId1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}/friends/{otherId}", parsedId1, parsedId2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}/friends/{otherId}", parsedId2, parsedId1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void negativeDeleteUsersFriends() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", -9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());


        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);
        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();
        Integer parsedId1 = JsonPath.read(result1.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}/friends/{otherId}", parsedId1, -9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveGetCommonFriends() throws Exception {
        User user = User.builder()
                .email("test@test.com")
                .login("test_t")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

        String userJson1 = objectMapper.writeValueAsString(user);

        user.setLogin("colacoca");

        String userJson2 = objectMapper.writeValueAsString(user);

        user.setLogin("cocacola");

        String userJson3 = objectMapper.writeValueAsString(user);

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andReturn();
        Integer parsedId1 = JsonPath.read(result1.getResponse().getContentAsString(), "id");
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andReturn();
        Integer parsedId2 = JsonPath.read(result2.getResponse().getContentAsString(), "id");
        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3))
                .andReturn();
        Integer parsedId3 = JsonPath.read(result3.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId1, parsedId3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/friends/{otherId}", parsedId2, parsedId3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}/friends/common/{otherId}", parsedId1, parsedId2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        JSONArray jsonArray = new JSONArray(result.getResponse().getContentAsString());
        JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
        Integer firstId = jsonObject1.getInt("id");

        assertEquals(parsedId3, firstId);
    }

}