package com.desafio.hotel.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API - Desafio Hotel")
                        .version("v1")
                        .description("API de controle de entrada e saída de hóspedes dentro de um hotel")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Link teste")
                        .url("https://teste.com"))
                .tags(
                        Arrays.asList(
                                new Tag().name("Hotel").description("Requisições")
                        )
                );
    }
}
