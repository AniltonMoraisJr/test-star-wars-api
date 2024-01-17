package com.example.starwarsplanetapi.service;

import com.example.starwarsplanetapi.domain.Planet;
import com.example.starwarsplanetapi.domain.QueryBuilder;
import com.example.starwarsplanetapi.repository.PlanetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanetService {
    private final PlanetRepository planetRepository;

    public Planet create(Planet planet) {
        return  this.planetRepository.save(planet);
    }

    public List<Planet> findPlanets(String climate, String terrain){
        Example<Planet> planetExample = QueryBuilder.makeQuery(new Planet(climate, terrain));
        return this.planetRepository.findAll(planetExample) ;
    }

    public Planet findById(Long id) {
        return this.planetRepository.findById(id).orElseThrow();
    }

    public Planet findByName(String name) {
        return this.planetRepository.findByName(name).orElseThrow();
    }

    public void removeById(Long id){
        planetRepository.deleteById(id);
    }
}
