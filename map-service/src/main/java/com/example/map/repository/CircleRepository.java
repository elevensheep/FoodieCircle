package com.example.map.repository;

import com.example.map.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CircleRepository extends JpaRepository<Circle, Long> {

    List<Circle> findByOwnerId(Long ownerId);
}
