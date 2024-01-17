package com.example.starwarsplanetapi.service;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.domain.QueryBuilder;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.starwarsplanetapi.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = {PlanetService.class, PlanetRepository.class})
@ExtendWith(MockitoExtension.class)
public class PlanetServiceTest {
//    @MockBean
    @Mock
    private PlanetRepository planetRepository;
//    @Autowired
    @InjectMocks
    private PlanetService planetService;

    // Regra de nome de métodos de teste = operacao_estado_retorno
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        // AAA - Como organizar o codigo dentro do método de teste

        // Arrange
        when(planetRepository.save(PLANET)).thenReturn(PLANET);

        // Act
        // System under test
        Planet sut = planetService.create(PLANET);

        // Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {
        when(planetRepository.save(INVALID_PLANET)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> planetService.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void findPlanet_WithExistId_ReturnPlanet(){
        when(planetRepository.findById(1L)).thenReturn(Optional.of(PLANET));

        Planet sut = planetService.findById(1L);

        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void findPlanet_WithUnexistId_ThrowsNotSuchElementException(){
        when(planetRepository.findById(290L)).thenThrow(new NoSuchElementException());

        assertThatThrownBy(() -> planetService.findById(290L)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void findPlanet_WithExistName_ReturnPlanet(){
        when(planetRepository.findByName(PLANET_TATOOINE.getName())).thenReturn(Optional.of(PLANET_TATOOINE));

        Planet sut = planetService.findByName(PLANET_TATOOINE.getName());

        assertThat(sut).isEqualTo(PLANET_TATOOINE);
        assertThat(sut.getName()).isEqualTo(PLANET_TATOOINE.getName());
    }

    @Test
    public void findPlanet_WithUnexistName_ThrowsNotSuchElementException(){
        when(planetRepository.findByName("Unexisting name")).thenThrow(new NoSuchElementException());

        assertThatThrownBy(() -> planetService.findByName("Unexisting name")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void listPlanets_ReturnAllPlanets() {
        when(planetRepository.findAll(QueryBuilder.makeQuery(new Planet()))).thenReturn(LIST_OF_PLANETS);

        List<Planet> sut = planetService.findPlanets(null, null);

        assertThat(sut).isEqualTo(LIST_OF_PLANETS);
        assertThat(sut).hasSize(3);
        assertThat(sut.get(0)).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void listPlanets_ReturnNoPlanets() {
        when(planetRepository.findAll(any())).thenReturn(Collections.emptyList());

        List<Planet> sut = planetService.findPlanets(null, null);

        assertThat(sut.size()).isEqualTo(0);
    }

    @Test
    public void removePlanet_WithExistId_ReturnPlanet(){
        assertThatCode(() -> planetService.removeById(1L)).doesNotThrowAnyException();
    }

    @Test
    public void removePlanet_WithUnexistId_ThrowsNotSuchElementException(){
        doThrow(new NoSuchElementException()).when(planetRepository).deleteById(290L);

        assertThatThrownBy(() -> planetService.removeById(290L)).isInstanceOf(NoSuchElementException.class);
    }


}
