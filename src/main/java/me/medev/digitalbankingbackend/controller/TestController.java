package me.medev.digitalbankingbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Controller", description = "Controller for testing Swagger configuration")
public class TestController {

    @GetMapping
    @Operation(summary = "Test endpoint", description = "This endpoint is used to test if the API is working")
    public String test() {
        return "API is working correctly!";
    }
}
