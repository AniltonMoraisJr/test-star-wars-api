package com.example.starwarsplanetapi.domain;


import com.example.starwarsplanetapi.jacoco.ExcludeFromJacocoGeneratedReport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planets")
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;

    @NotEmpty
    @Column(nullable = false)
    private String climate;

    @NotEmpty
    @Column(nullable = false)
    private String terrain;

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String climate, String terrain) {
        this.climate = climate;
        this.terrain = terrain;
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", climate='" + climate + '\'' +
                ", terrain='" + terrain + '\'' +
                '}';
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planet planet = (Planet) o;
        return Objects.equals(id, planet.id) && Objects.equals(name, planet.name) && Objects.equals(climate, planet.climate) && Objects.equals(terrain, planet.terrain);
    }

    @ExcludeFromJacocoGeneratedReport
    @Override
    public int hashCode() {
        return Objects.hash(id, name, climate, terrain);
    }
}
