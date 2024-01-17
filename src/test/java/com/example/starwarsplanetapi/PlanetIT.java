package com.example.starwarsplanetapi;

import com.example.starwarsplanetapi.domain.Planet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET;
import static com.example.starwarsplanetapi.common.PlanetConstants.PLANET_TATOOINE;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("it") // Seleciona o profile application-it.properties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Gera uma porta aleatória ao subir app
@Sql(scripts = {"/import_script.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // Executa script antes de cada método de teste
@Sql(scripts = {"/remove_planets_script.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // Executa script depois de cada método de teste
public class PlanetIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void createPlanet_ReturnsCreated() {
        ResponseEntity<Planet> sut = restTemplate.postForEntity("/planets", PLANET, Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(sut.getBody().getId()).isNotNull();
        assertThat(sut.getBody().getName()).isEqualTo(PLANET.getName());
        assertThat(sut.getBody().getClimate()).isEqualTo(PLANET.getClimate());
        assertThat(sut.getBody().getTerrain()).isEqualTo(PLANET.getTerrain());
    }

    @Test
    public void getPlanet_ReturnsPlanet() {
        ResponseEntity<Planet> sut =  restTemplate.getForEntity("/planets/1",  Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void getPlanetByName_ReturnsPlanet() {
        ResponseEntity<Planet> sut =  restTemplate.getForEntity("/planets/name/" + PLANET_TATOOINE.getName(),  Planet.class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void listAllPlanets_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut =  restTemplate.getForEntity("/planets/",  Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(3);
        assertThat(sut.getBody()[0]).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void listAllPlanets_ByClimate_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut =  restTemplate.getForEntity("/planets/?climate="+PLANET_TATOOINE.getClimate(),  Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void listAllPlanets_ByTerrain_ReturnsPlanets() {
        ResponseEntity<Planet[]> sut =  restTemplate.getForEntity("/planets/?terrain="+PLANET_TATOOINE.getTerrain(),  Planet[].class);

        assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sut.getBody()).hasSize(1);
        assertThat(sut.getBody()[0]).isEqualTo(PLANET_TATOOINE);
    }

    @Test
    public void deletePlanet_ReturnNoContent() {
      ResponseEntity<Void> sut = restTemplate.exchange("/planets/"+PLANET_TATOOINE.getId(), HttpMethod.DELETE, null, Void.class);
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
