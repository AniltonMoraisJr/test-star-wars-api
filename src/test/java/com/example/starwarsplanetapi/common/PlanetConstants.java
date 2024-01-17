package com.example.starwarsplanetapi.common;

import com.example.starwarsplanetapi.domain.Planet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanetConstants {
    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet PLANET_TATOOINE = new Planet(1L,"Tatooine", "arid", "desert");
    public static final Planet PLANET_ALDERAAN = new Planet(2L ,"Alderaan", "temperate", "grasslands, mountains");
    public static final Planet PLANET_YAVINIV = new Planet(3L, "Yavin IV", "temperate, tropical", "jungle, rainforests");
    public static final Planet PLANET_HOTH = new Planet(4L, "Hoth", "frozen", "tundra, ice caves, mountain ranges");
    public static final List<Planet> PLANETS = new ArrayList<>() {
        {
            add(PLANET_TATOOINE);
            add(PLANET_ALDERAAN);
            add(PLANET_YAVINIV);
        }
    };
    public static final Planet INVALID_PLANET = new Planet("", "", "");
    public static final Planet EMPTY_PLANET = new Planet();
    public static final List<Planet> LIST_OF_PLANETS = Arrays.asList(PLANET_TATOOINE, PLANET_ALDERAAN, PLANET_HOTH);
}
