package com.example.starwarsplanetapi.web;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.service.PlanetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/planets", produces = {"application/json"})
@RequiredArgsConstructor
public class PlanetController {

    private final PlanetService planetService;

    @GetMapping(value = "/", produces = {"application/json"})
    public ResponseEntity<List<Planet>> getPlanets(@RequestParam(name = "climate", required = false) String climate,
                                                   @RequestParam(name = "terrain", required = false) String terrain) {
        List<Planet> planetsFounded = planetService.findPlanets(climate, terrain);
        return ResponseEntity.ok(planetsFounded);
    }

    @PostMapping
    public ResponseEntity<Planet> create(@Valid @RequestBody Planet planet) {
        Planet planetCreated = planetService.create(planet);
        return ResponseEntity.status(HttpStatus.CREATED).body(planetCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> getById(@PathVariable Long id) {
        Planet planetFounded = planetService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(planetFounded);

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable String name) {
        Planet planetFounded = planetService.findByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(planetFounded);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Planet> removeById(@PathVariable Long id) {
        this.planetService.removeById(id);
        return ResponseEntity.noContent().build();
    }

}
