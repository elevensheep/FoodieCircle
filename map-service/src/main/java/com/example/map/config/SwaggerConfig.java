package com.example.map.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodieCircle Map Service API")
                        .description("서클(그룹) 관리, 마커 등록, 위치 기반 로직 API")
                        .version("v1.0.0"));
    }
}
