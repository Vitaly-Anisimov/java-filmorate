package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"TestUsername\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson.toString()));
    }

    @Test
    public void positivePostWithEmptyNameShouldReturnCode200() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");


        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"email\":\"test@test.com\",\n");
        userJson1.append("\"login\":\"test_t\",\n");
        userJson1.append("\"name\":\"test_t\",\n");
        userJson1.append("\"birthday\":\"2001-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson1.toString()));
    }

    @Test
    public void negativePostWithNoCorrectEmailShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test12312 com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"TestUsername\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"email\":\"\",\n");
        userJson1.append("\"login\":\"test_t\",\n");
        userJson1.append("\"name\":\"TestUsername\",\n");
        userJson1.append("\"birthday\":\"2001-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson2 = new StringBuilder();
        userJson2.append("{\n");
        userJson2.append("\"email\":\"              \",\n");
        userJson2.append("\"login\":\"test_t\",\n");
        userJson2.append("\"name\":\"TestUsername\",\n");
        userJson2.append("\"birthday\":\"2001-11-11\"\n");
        userJson2.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectLoginShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test login\",\n");
        userJson.append("\"name\":\"TestUsername\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectBirthDayShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"testlogin\",\n");
        userJson.append("\"name\":\"TestUsername\",\n");
        userJson.append("\"birthday\":\"3001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveUpdateShoudReturnCode200() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"TestUsername\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson.toString()));

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"id\": 1,\n");
        userJson1.append("\"email\":\"test1@test.com\",\n");
        userJson1.append("\"login\":\"test_t1\",\n");
        userJson1.append("\"name\":\"TestUsername1\",\n");
        userJson1.append("\"birthday\":\"2002-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson1.toString()));
    }

    @Test
    public void positiveUpdateWithEmptyNameShoudReturnCode200() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        StringBuilder userJson2 = new StringBuilder();
        userJson2.append("{\n");
        userJson2.append("\"email\":\"test@test.com\",\n");
        userJson2.append("\"login\":\"test_t\",\n");
        userJson2.append("\"name\":\"test_t\",\n");
        userJson2.append("\"birthday\":\"2001-11-11\"\n");
        userJson2.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson2.toString()));

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"id\": 1,\n");
        userJson1.append("\"email\":\"test1@test.com\",\n");
        userJson1.append("\"login\":\"test_t1\",\n");
        userJson1.append("\"name\":\"TestUsername1\",\n");
        userJson1.append("\"birthday\":\"2002-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson1.toString()));
    }

    @Test
    public void negativeUpdateWithNoEmptyLoginShoudReturnCode200() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"test_t\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson.toString()));

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"id\": 1,\n");
        userJson1.append("\"email\":\"test1@test.com\",\n");
        userJson1.append("\"login\":\"\",\n");
        userJson1.append("\"name\":\"TestUsername1\",\n");
        userJson1.append("\"birthday\":\"2002-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectBirthdayShoudReturnCode200() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"email\":\"test@test.com\",\n");
        userJson.append("\"login\":\"test_t\",\n");
        userJson.append("\"name\":\"test_t\",\n");
        userJson.append("\"birthday\":\"2001-11-11\"\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson.toString()));

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"id\": 1,\n");
        userJson1.append("\"email\":\"test1@test.com\",\n");
        userJson1.append("\"login\":\"\",\n");
        userJson1.append("\"name\":\"TestUsername1\",\n");
        userJson1.append("\"birthday\":\"3002-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateNotInRepository() throws Exception {
        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"id\": 1,\n");
        userJson1.append("\"email\":\"test1@test.com\",\n");
        userJson1.append("\"login\":\"\",\n");
        userJson1.append("\"name\":\"TestUsername1\",\n");
        userJson1.append("\"birthday\":\"3002-11-11\"\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}