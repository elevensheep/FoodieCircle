package com.example.FoodiCircle.controller;

import com.example.FoodiCircle.dto.ApiResponse;
import com.example.FoodiCircle.service.HomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/health")
    public ApiResponse<Void> health() {
        return ApiResponse.success("OK");
    }

    @GetMapping("/home")
    public ApiResponse<String> home() {
        return ApiResponse.success("OK", homeService.getWelcomeMessage());
    }
}
