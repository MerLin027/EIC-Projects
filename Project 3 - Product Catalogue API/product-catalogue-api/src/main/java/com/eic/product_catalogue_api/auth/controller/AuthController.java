package com.eic.product_catalogue_api.auth.controller;

import com.eic.product_catalogue_api.auth.dto.AuthResponse;
import com.eic.product_catalogue_api.auth.dto.LoginRequest;
import com.eic.product_catalogue_api.auth.dto.RegisterRequest;
import com.eic.product_catalogue_api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Users")
public class AuthController
{
    private final AuthService authService;
    // POST /auth/register
    @PostMapping("/register")
    @Operation(summary = "Registers a new user")
    public ResponseEntity <AuthResponse> createUser(@Valid @RequestBody RegisterRequest request)
    {
        AuthResponse response = authService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // POST /auth/login
    @PostMapping("/login")
    @Operation(summary = "Logs in and returns a JWT")
    public ResponseEntity <AuthResponse> login(@Valid @RequestBody LoginRequest request)
    {
        AuthResponse response = authService.verify(request);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
