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
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {
    private static final String PATH = "/films";
    private static final String LARGE_STRING = "3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7d"
            + "y0BhfhQwBqgA87d3kpBz0o1Lu0bJZBC9DYhfOpuzdVAuFAW29ZiFTJAPDy2ca7CwuMsE1dD6UKGy8Vqf1t9Co45JuDCS0zO3wM7dy0BhfhQwBqgA87dasdasdasdada";
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUpTest() {
        FilmController filmController;

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2011, 11, 11))
                .duration(120)
                .build();

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
        Film film = Film.builder()
                .name(null)
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2011, 11, 11))
                .duration(120)
                .build();

        String filmJson1 = objectMapper.writeValueAsString(film);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void negativePostWithNoCorrectDesciptionShouldReturn400() throws Exception {
        Film film = Film.builder()
                .name(null)
                .description(LARGE_STRING)
                .releaseDate(LocalDate.of(2011, 11, 11))
                .duration(120)
                .build();

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
        Film film = Film.builder()
                .name(null)
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(120)
                .build();

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(0)
                .build();

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

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
        film.setReleaseDate(LocalDate.of(2001, 9, 1));
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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

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
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

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

    @Test
    public void positiveGetByFilmByIdShouldReturn200() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", parsedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void negativeGetByFilmByIdShouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", -9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveAddFilmLikeShouldReturn200() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void negativeAddFilmLikeShouldReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", 9999, 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveDeleteFilmLike() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        int userId = 10;

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, userId)
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", parsedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("likes", hasSize(1)));
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}/like/{userId}", parsedId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", parsedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("likes", hasSize(0)));
    }

    @Test
    public void positiveAddFilmLikeDoubleShouldReturnSize1() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();

        String filmJson1 = objectMapper.writeValueAsString(film);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filmJson1))
                .andReturn();

        Integer parsedId = JsonPath.read(result.getResponse().getContentAsString(), "id");
        Integer[] usersId = new Integer[1];
        usersId[0] = 10;

        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, usersId[0])
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, usersId[0])
                .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", parsedId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("likes", hasSize(1)));
    }

    @Test
    public void negativeDeleteFilmLikeShouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}/like/{userId}", 9999, 9999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void positiveGetPopularFilmsWithParamCount() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();
        Integer parsedId = null;
        String filmJson1 = objectMapper.writeValueAsString(film);
        int sizeArray = 6;

        for (int i = 0; i < sizeArray; i++) {
            MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(filmJson1))
                    .andReturn();
            Thread.sleep(100);

            if (i == 2) {
                parsedId = JsonPath.read(result1.getResponse().getContentAsString(), "id");
                mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, 10)
                        .contentType(MediaType.APPLICATION_JSON));
                Thread.sleep(100);
                mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, 20)
                        .contentType(MediaType.APPLICATION_JSON));
            }
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/popular").param("count", String.valueOf(sizeArray))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        JSONArray jsonArray = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(sizeArray, jsonArray.length());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        Integer firstId = jsonObject.getInt("id");
        assertEquals(parsedId, firstId);
    }

    @Test
    public void positiveGetPopularFilmsWithoutParamCount() throws Exception {
        Film film = Film.builder()
                .name("Форсаж")
                .description("Лучший боевик столетия")
                .releaseDate(LocalDate.of(2005, 12, 28))
                .duration(20)
                .build();
        Integer parsedId = null;
        String filmJson1 = objectMapper.writeValueAsString(film);
        int sizeArray = 11;

        for (int i = 0; i < sizeArray; i++) {
            MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(filmJson1))
                    .andReturn();
            Thread.sleep(100);

            if (i == 2) {
                parsedId = JsonPath.read(result1.getResponse().getContentAsString(), "id");
                mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}/like/{userId}", parsedId, 10)
                        .contentType(MediaType.APPLICATION_JSON));
            }
        }

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        JSONArray jsonArray = new JSONArray(result.getResponse().getContentAsString());
        assertEquals(sizeArray - 1, jsonArray.length());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        Integer firstId = jsonObject.getInt("id");
        assertEquals(parsedId, firstId);
    }


}