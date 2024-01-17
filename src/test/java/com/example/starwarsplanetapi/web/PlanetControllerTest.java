package com.example.starwarsplanetapi.web;

import com.example.starwarsplanetapi.service.PlanetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.example.starwarsplanetapi.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlanetController.class)
public class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PlanetService planetService;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception {

        when(planetService.create(PLANET_ALDERAAN)).thenReturn(PLANET_ALDERAAN);

        mockMvc
                .perform(post("/planets")
                            .content(objectMapper.writeValueAsString(PLANET_ALDERAAN))
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET_ALDERAAN));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnBadRequest() throws Exception {
        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(EMPTY_PLANET))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());

        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(INVALID_PLANET))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception {
        when(planetService.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc
                .perform(post("/planets")
                        .content(objectMapper.writeValueAsString(PLANET_TATOOINE))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_WithExistingId_ReturnsPlanet() throws Exception {
        when(planetService.findById(1L)).thenReturn(PLANET_ALDERAAN);

        mockMvc
                .perform(get("/planets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET_ALDERAAN));
    }

    @Test
    public void getPlanet_WithUnexistingId_ReturnsNotFound() throws Exception {

        when(planetService.findById(1L)).thenThrow(NoSuchElementException.class);

        mockMvc
                .perform(get("/planets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPlanet_WithExistingName_ReturnsPlanet() throws Exception {
        when(planetService.findByName(PLANET_ALDERAAN.getName())).thenReturn(PLANET_ALDERAAN);

        mockMvc
                .perform(get(String.format("/planets/name/%s", PLANET_ALDERAAN.getName()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET_ALDERAAN));
    }

    @Test
    public void getPlanet_WithUnexistingName_ReturnsNotFound() throws Exception {

        when(planetService.findByName("teste")).thenThrow(NoSuchElementException.class);

        mockMvc
                .perform(get("/planets/name/teste")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception {
        when(planetService.findPlanets(null, null)).thenReturn(LIST_OF_PLANETS);
        when(planetService.findPlanets(PLANET_ALDERAAN.getClimate(), PLANET_ALDERAAN.getTerrain())).thenReturn(Arrays.asList(PLANET_ALDERAAN));

        mockMvc
                .perform(get("/planets/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value(LIST_OF_PLANETS.get(0)));
        mockMvc
                .perform(get("/planets/?" + String.format("climate=%s&terrain=%s", PLANET_ALDERAAN.getClimate(), PLANET_ALDERAAN.getTerrain()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").value(PLANET_ALDERAAN));

    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {
        when(planetService.findPlanets("dry", null)).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/planets/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDataBase() throws Exception{
        mockMvc
                .perform(delete("/planets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void removePlanet_WithUnexistingId_RemovesPlanetFromDataBase() throws Exception{

        doThrow(new EmptyResultDataAccessException(2)).when(planetService).removeById(2L);

        mockMvc
                .perform(delete("/planets/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
