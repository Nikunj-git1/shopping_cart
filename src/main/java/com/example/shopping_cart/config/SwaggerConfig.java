package com.example.shopping_cart.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        List<Tag> tags = new LinkedList<>();

        // For order to UI view of APIs
        tags.add(new Tag().name("Admin API").description("Signup and Login"));
        tags.add(new Tag().name("Category API").description("For CRUD operation of categories"));
        tags.add(new Tag().name("Sub-category API").description("For CRUD operation of sub-categories"));
        tags.add(new Tag().name("Item API").description("For CRUD operation of items"));
        tags.add(new Tag().name("Customer API").description("For CRUD operation of customers"));
        tags.add(new Tag().name("Order API").description("For CRUD operation of orders"));

        return new OpenAPI()
                .info(new Info()
                        .title("Shopping Cart API")
                        .version("1.0")
                        .description("To test JWT-protected APIs using Swagger"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("http://localhost:8081").description("Live")))
                .tags(tags)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)));
    }

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("all-apis")
                .pathsToMatch(
                        "/admin/**",
                        "/admin/cat/**",
                        "/admin/sub-cat/**",
                        "/admin/item/**",
                        "/admin/order/**",
                        "/customer/**",
                        "/customer/item/**")
                .build();
    }

//    @Bean
//    public OpenApiCustomizer tagSorterCustomizer() {
//        List<String> tagOrder = Arrays.asList(
//                "Admin API",
//                "Category API",
//                "Sub-category API",
//                "Item API",
//                "Customer API",
//                "Order API"
//        );
//
//        return openApi -> {
//            if (openApi.getTags() == null) return;
//
//            List<Tag> sortedTags = new LinkedList<>();
//            for (String tagName : tagOrder) {
//                openApi.getTags().stream()
//                        .filter(tag -> tag.getName().equals(tagName))
//                        .findFirst()
//                        .ifPresent(sortedTags::add);
//            }
//
//            openApi.setTags(sortedTags);
//        };
//    }

//    @Bean
//    public OpenApiCustomizer redocTagGroupCustomizer() {
//        return openApi -> {
//            Map<String, Object> tagGroups = new LinkedHashMap<>();
//            tagGroups.put("x-tagGroups", List.of(
//                    Map.of("name", "1. Users", "tags", List.of("Users")),
//                    Map.of("name", "2. Products", "tags", List.of("Products")),
//                    Map.of("name", "3. Orders", "tags", List.of("Orders"))
//            ));
//            openApi.setExtensions(tagGroups);
//        };
//    }
}