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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {
    private static final String PATH = "/films";
    private static final String LARGE_STRING = "3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7d"
            + "y0BhfhQwBqgA87d3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87dasdasdasdada";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpTest() {
        FilmController filmController = new FilmController();
    }

    @Test
    public void positiveGetAllEmptyShouldReturnCode200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void positivePostShouldReturnCode200() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2011, 11, 11), 120);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()));
    }

    @Test
    public void negativePostWithNoCorrectNameShouldReturn400() throws Exception {
        Film film = new Film(null, "Лучший боевик столетия", LocalDate.of(2011, 11, 11), 120);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectDesciptionShouldReturn400() throws Exception {
        Film film = new Film(null, LARGE_STRING, LocalDate.of(2011, 11, 11), 120);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDescription("                                 ");

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDescription(null);

        String filmJson3 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectRealeseDateShouldReturn400() throws Exception {
        Film film = new Film(null, "Лучший боевик столетия", LocalDate.of(1895, 12, 28), 120);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setReleaseDate(LocalDate.of(1000, 11, 11));

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectDurationShouldReturn400() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 0);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDuration(-2);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveUpdateShouldReturn200() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 20);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        film.setId(parsedId.longValue());
        film.setName("Форсаж 2");
        film.setDescription("Так себе");
        film.setReleaseDate(LocalDate.of(2001, 9, 01));
        film.setDuration(120);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("id").value(film.getId()))
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()));
    }

    @Test
    public void negativeUpdateWithNoCorrectNameShouldReturn400() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 20);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        film.setId(parsedId.longValue());
        film.setName(null);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        film.setName("                                                 ");

        String filmJson3 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDescriptionShouldReturn400() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 20);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        film.setId(parsedId.longValue());
        film.setDescription(LARGE_STRING);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDescription("                         ");

        String filmJson3 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDescription(null);

        String filmJson4 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson4))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDataReleaseShouldReturn400() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 20);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        film.setId(parsedId.longValue());
        film.setReleaseDate(null);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        String filmJson3 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setReleaseDate(LocalDate.of(1001, 12, 28));

        String filmJson4 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson4))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativeUpdateWithNoCorrectDurationShouldReturn400() throws Exception {
        Film film = new Film("Форсаж", "Лучший боевик столетия", LocalDate.of(2005, 12, 28), 20);
        ObjectMapper objectMapper = new ObjectMapper();
        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("name").value(film.getName()))
                .andExpect(jsonPath("description").value(film.getDescription()))
                .andExpect(jsonPath("releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(jsonPath("duration").value(film.getDuration()))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        film.setId(parsedId.longValue());
        film.setDuration(0);

        String filmJson2 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson2))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        film.setDuration(-2);

        String filmJson3 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson3))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}