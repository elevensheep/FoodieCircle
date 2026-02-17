package com.example.map.repository;

import com.example.map.entity.CircleGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<CircleGroup, Long> {

    List<CircleGroup> findByOwnerId(Long ownerId);
}
