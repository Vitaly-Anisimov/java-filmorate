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

class FilmControllerTest {
    private static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpTest() {
        FilmController userController = new FilmController();
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
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2001-11-11\",\n");
        userJson.append("\"duration\":120\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userJson.toString()));
    }

    @Test
    public void negativePostWithNoCorrectNameShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"name\":\"\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2001-11-11\",\n");
        userJson.append("\"duration\":120\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"name\":\"         \",\n");
        userJson1.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson1.append("\"releaseDate\":\"2001-11-11\",\n");
        userJson1.append("\"duration\":120\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectDesciptionShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87d");
        userJson.append("3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87dasdasdasdada\"\n");
        userJson.append("\"releaseDate\":\"2001-11-11\",\n");
        userJson.append("\"duration\":120\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"name\":\"Форсаж\",\n");
        userJson1.append("\"description\":\"                     \",\n");
        userJson1.append("\"releaseDate\":\"2001-11-11\",\n");
        userJson1.append("\"duration\":120\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectRealeseDateShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();
        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"1895-12-28\",\n");
        userJson.append("\"duration\":120\n");
        userJson.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"name\":\"Форсаж\",\n");
        userJson1.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson1.append("\"releaseDate\":\"1000-11-11\",\n");
        userJson1.append("\"duration\":120\n");
        userJson1.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectDurationShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":0\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson1 = new StringBuilder();
        userJson1.append("{\n");
        userJson1.append("\"name\":\"Форсаж\",\n");
        userJson1.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson1.append("\"releaseDate\":\"1000-11-11\",\n");
        userJson1.append("\"duration\":-2\n");
        userJson1.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson2 = new StringBuilder();
        userJson2.append("{\n");
        userJson2.append("\"name\":\"Форсаж\",\n");
        userJson2.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson2.append("\"releaseDate\":\"1000-11-11\",\n");
        userJson2.append("\"duration\":\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveUpdateShouldReturn200() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":100\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        StringBuilder userJson2 = new StringBuilder();

        userJson2.append("{\n");
        userJson2.append("\"id\": 1,\n");
        userJson2.append("\"name\":\"Форсаж2\",\n");
        userJson2.append("\"description\":\"Так себе\",\n");
        userJson2.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson2.append("\"duration\":120\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(userJson2.toString()));
    }

    @Test
    public void negativeUpdateWithNoCorrectNameShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":100\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        StringBuilder userJson2 = new StringBuilder();

        userJson2.append("{\n");
        userJson2.append("\"id\": 1,\n");
        userJson2.append("\"name\":\"\",\n");
        userJson2.append("\"description\":\"Так себе\",\n");
        userJson2.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson2.append("\"duration\":120\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson3 = new StringBuilder();

        userJson3.append("{\n");
        userJson3.append("\"id\": 1,\n");
        userJson3.append("\"name\":\"           \",\n");
        userJson3.append("\"description\":\"Так себе\",\n");
        userJson3.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson3.append("\"duration\":120\n");
        userJson3.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDescriptionShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":100\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        StringBuilder userJson2 = new StringBuilder();

        userJson2.append("{\n");
        userJson2.append("\"id\": 1,\n");
        userJson2.append("\"name\":\"Лучший боевик столетия\",\n");
        userJson.append("\"description\":\"3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87d");
        userJson.append("3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87dasdasdasdada\"\n");
        userJson2.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson2.append("\"duration\":120\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson3 = new StringBuilder();

        userJson3.append("{\n");
        userJson3.append("\"id\": 1,\n");
        userJson3.append("\"name\":\"Лучший боевик столетия\",\n");
        userJson3.append("\"description\":\"\",\n");
        userJson3.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson3.append("\"duration\":120\n");
        userJson3.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson4 = new StringBuilder();

        userJson4.append("{\n");
        userJson4.append("\"id\": 1,\n");
        userJson4.append("\"name\":\"Лучший боевик столетия\",\n");
        userJson4.append("\"description\":\"              \",\n");
        userJson4.append("\"releaseDate\":\"2001-09-11\",\n");
        userJson4.append("\"duration\":120\n");
        userJson4.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson4.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDataReleaseShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":100\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        StringBuilder userJson2 = new StringBuilder();

        userJson2.append("{\n");
        userJson2.append("\"id\": 1,\n");
        userJson2.append("\"name\":\"Форсаж\",\n");
        userJson2.append("\"description\":\"Так себе\",\n");
        userJson2.append("\"releaseDate\":\"\",\n");
        userJson2.append("\"duration\":120\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson3 = new StringBuilder();

        userJson3.append("{\n");
        userJson3.append("\"id\": 1,\n");
        userJson3.append("\"name\":\"Форсаж\",\n");
        userJson3.append("\"description\":\"Так себе\",\n");
        userJson3.append("\"releaseDate\":\"1895-12-28\",\n");
        userJson3.append("\"duration\":120\n");
        userJson3.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDurationShouldReturn400() throws Exception {
        StringBuilder userJson = new StringBuilder();

        userJson.append("{\n");
        userJson.append("\"name\":\"Форсаж\",\n");
        userJson.append("\"description\":\"Лучший боевик столетия\",\n");
        userJson.append("\"releaseDate\":\"2020-01-01\",\n");
        userJson.append("\"duration\":100\n");
        userJson.append("}");
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson.toString()))
                        .andExpect(MockMvcResultMatchers.status().isOk());

        StringBuilder userJson2 = new StringBuilder();

        userJson2.append("{\n");
        userJson2.append("\"id\": 1,\n");
        userJson2.append("\"name\":\"Форсаж\",\n");
        userJson2.append("\"description\":\"Так себе\",\n");
        userJson2.append("\"releaseDate\":\"\",\n");
        userJson2.append("\"duration\":0\n");
        userJson2.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2.toString()))
                        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson3 = new StringBuilder();

        userJson3.append("{\n");
        userJson3.append("\"id\": 1,\n");
        userJson3.append("\"name\":\"Форсаж\",\n");
        userJson3.append("\"description\":\"Так себе\",\n");
        userJson3.append("\"releaseDate\":\"\",\n");
        userJson3.append("\"duration\":-100\n");
        userJson3.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson3.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        StringBuilder userJson4 = new StringBuilder();

        userJson4.append("{\n");
        userJson4.append("\"id\": 1,\n");
        userJson4.append("\"name\":\"Форсаж\",\n");
        userJson4.append("\"description\":\"Так себе\",\n");
        userJson4.append("\"releaseDate\":\"\",\n");
        userJson4.append("\"duration\":\n");
        userJson4.append("}");
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson4.toString()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}