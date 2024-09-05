package com.example.potatoStudy_jwt.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi dafaultApi() {
        Info info = new Info().title("감자 JWT 스터디 API").version("v0.1");

        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .displayName("All API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        Info info = new Info().title("감자 JWT 스터디 유저 API").version("v0.1");

        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/user/**")
                .displayName("USER API")
                .addOpenApiCustomizer(api -> api.setInfo(info))
                .build();
    }

}
