package com.example.map.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CircleGroupTest {

    @Test
    void createCircleGroup_setsFieldsCorrectly() {
        CircleGroup group = new CircleGroup();
        group.setName("Work Lunch");
        group.setOwnerId(1L);

        assertThat(group.getName()).isEqualTo("Work Lunch");
        assertThat(group.getOwnerId()).isEqualTo(1L);
    }

    @Test
    void createdAt_isSetAutomatically() {
        CircleGroup group = new CircleGroup();
        group.onCreate();

        assertThat(group.getCreatedAt()).isNotNull();
        assertThat(group.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }
}
