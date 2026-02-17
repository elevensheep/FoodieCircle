package com.example.map.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Double y;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Restaurant(String externalId, String name, String address, String category, Double x, Double y) {
        this.externalId = externalId;
        this.name = name;
        this.address = address;
        this.category = category;
        this.x = x;
        this.y = y;
        this.createdAt = LocalDateTime.now();
    }

    public static Restaurant create(String externalId, String name, String address, String category, Double x, Double y) {
        return new Restaurant(externalId, name, address, category, x, y);
    }
}
