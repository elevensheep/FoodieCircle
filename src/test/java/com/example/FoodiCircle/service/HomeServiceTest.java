package com.example.FoodiCircle.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HomeServiceTest {

    private final HomeService homeService = new HomeService();

    @Test
    void getWelcomeMessage_returnsExpectedMessage() {
        String message = homeService.getWelcomeMessage();

        assertThat(message).isEqualTo("Welcome to FoodiCircle!");
    }

    @Test
    void getWelcomeMessage_isNotBlank() {
        String message = homeService.getWelcomeMessage();

        assertThat(message).isNotBlank();
    }
}
