package com.example.starwarsplanetapi.repository;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.domain.QueryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.starwarsplanetapi.common.PlanetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach() {
        PLANET_ALDERAAN.setId(null);
    }

    @Test
    public void createPlanet_WithValidData_ReturnPlanet() {
        Planet planet = planetRepository.save(PLANET_TATOOINE);

        Planet sut = testEntityManager.find(Planet.class, planet.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(planet.getName());
        assertThat(sut.getClimate()).isEqualTo(planet.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(planet.getTerrain());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPlanets")
    public void createPlanet_WithInvalidData_ThrowsException(Planet planet) {
        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    private static Stream<Arguments> provideInvalidPlanets() {
        return Stream.of(
                Arguments.of(new Planet(null, "climate", "terrain")),
                Arguments.of(new Planet("name", null, "terrain")),
                Arguments.of(new Planet("name", "climate", null)),
                Arguments.of(new Planet("name", null, null)),
                Arguments.of(new Planet(null, null, null)),
                Arguments.of(new Planet("", "", "")),
                Arguments.of(new Planet("", "climate", "terrain")),
                Arguments.of(new Planet("", "", "terrain")),
                Arguments.of(new Planet("name", "", "")),
                Arguments.of(new Planet("", "climate", ""))
        );
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        Planet planet = testEntityManager.persistFlushFind(PLANET_ALDERAAN);
        testEntityManager.detach(planet);
        planet.setId(null);

        assertThatThrownBy(() -> planetRepository.save(planet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_WithExistingId_ReturnsPlanet() throws Exception {
        Planet planet = testEntityManager.persistFlushFind(PLANET_ALDERAAN);

        Optional<Planet> sut = planetRepository.findById(planet.getId());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get().getName()).isEqualTo(planet.getName());
    }

    @Test
    public void getPlanet_WithUnexistingId_ReturnsNotFound() throws Exception {
        Optional<Planet> planetOptional = planetRepository.findById(1L);

        assertThat(planetOptional).isEmpty();
    }

    @Test
    public void getPlanet_WithExistingName_ReturnsPlanet() throws Exception {
        Planet planet = testEntityManager.persistFlushFind(PLANET_ALDERAAN);

        Optional<Planet> sut = planetRepository.findByName(planet.getName());

        assertThat(sut).isNotEmpty();
        assertThat(sut.get().getName()).isEqualTo(planet.getName());
    }

    @Test
    public void getPlanet_WithUnexistingName_ReturnsNotFound() throws Exception {
        Optional<Planet> planetOptional = planetRepository.findByName("teste");

        assertThat(planetOptional).isEmpty();
    }

    @Sql(scripts = "/import_script.sql") // Execute script before method execution
    @Test
    public void listPlanets_ReturnsFilteredPlanets() throws Exception {
        Example<Planet> exampleWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> exampleWithFilters = QueryBuilder.makeQuery(new Planet(PLANET_YAVINIV.getClimate(), PLANET_YAVINIV.getTerrain()));

        List<Planet> planetListWithoutFilters = planetRepository.findAll(exampleWithoutFilters);
        List<Planet> planetListWithFilters = planetRepository.findAll(exampleWithFilters);

        assertThat(planetListWithoutFilters).isNotEmpty();
        assertThat(planetListWithoutFilters).hasSize(3);
        assertThat(planetListWithFilters).isNotEmpty();
        assertThat(planetListWithFilters).hasSize(1);
        assertThat(planetListWithFilters.get(0)).isEqualTo(PLANET_YAVINIV);

    }

    @Test
    public void listPlanets_ReturnsNoPlanets() throws Exception {

        Example<Planet> example = QueryBuilder.makeQuery(new Planet());

        List<Planet> planetList = planetRepository.findAll(example);

        assertThat(planetList).isEmpty();
    }

    @Test
    public void removePlanet_WithExistingId_RemovesPlanetFromDataBase() {
        Planet planet = testEntityManager.persistFlushFind(PLANET_ALDERAAN);

        planetRepository.deleteById(planet.getId());

        Planet removedPlanet = testEntityManager.find(Planet.class, planet.getId());

        assertThat(removedPlanet).isNull();
    }

//    @Test
//    public void removePlanet_WithUnExistingId_ThrowsEmptyResultDataAccessException() {
//        assertThatThrownBy(() -> planetRepository.deleteById(6L)).isInstanceOf(EmptyResultDataAccessException.class);
//    }
}
