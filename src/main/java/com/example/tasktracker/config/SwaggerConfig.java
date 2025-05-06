package com.example.tasktracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springdoc.core.models.GroupedOpenApi;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Task Tracker API").version("1.0")
                        .description("API for task tracking"));
    }

    @Bean
    public GroupedOpenApi taskTrackerApi() {
        return GroupedOpenApi.builder()
                .group("task-tracker-api")
                .packagesToScan("com.example.tasktracker.controller")
                .build();
    }
}
