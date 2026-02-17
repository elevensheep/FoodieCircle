package com.example.map.controller;

import com.example.map.dto.RestaurantSearchResponse;
import com.example.map.service.RestaurantSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RestaurantSearchService restaurantSearchService;

    @InjectMocks
    private RestaurantSearchController restaurantSearchController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantSearchController).build();
    }

    @Test
    @DisplayName("GET /api/map/restaurants/search 성공")
    void searchSuccess() throws Exception {
        RestaurantSearchResponse response = new RestaurantSearchResponse(
                "K1", "맛집", "서울시", "한식", 127.0, 37.5, 5);

        given(restaurantSearchService.search("맛집", 127.0, 37.5, 1000))
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/map/restaurants/search")
                        .param("keyword", "맛집")
                        .param("x", "127.0")
                        .param("y", "37.5")
                        .param("radius", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("식당 검색 성공"))
                .andExpect(jsonPath("$.data[0].externalId").value("K1"))
                .andExpect(jsonPath("$.data[0].reviewCount").value(5));
    }
}
