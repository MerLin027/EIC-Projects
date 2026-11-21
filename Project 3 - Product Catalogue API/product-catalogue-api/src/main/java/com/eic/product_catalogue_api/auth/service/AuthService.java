package com.eic.product_catalogue_api.auth.service;

import com.eic.product_catalogue_api.auth.dto.AuthResponse;
import com.eic.product_catalogue_api.auth.dto.LoginRequest;
import com.eic.product_catalogue_api.auth.dto.RegisterRequest;
import com.eic.product_catalogue_api.auth.entity.MyUser;
import com.eic.product_catalogue_api.auth.mapper.AuthMapper;
import com.eic.product_catalogue_api.auth.repository.UserRepository;
import com.eic.product_catalogue_api.auth.security.JwtService;
import com.eic.product_catalogue_api.product.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final UserRepository repository;
    private final AuthMapper mapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${jwt.expiration-ms}")
    private Long expirationTime;

    //
    public AuthResponse create(RegisterRequest request) throws DuplicateResourceException
    {
        MyUser user  = mapper.toEntity(request);
        String email = user.getEmail().trim().toLowerCase();
        if(repository.existsByEmail(user.getEmail()))
            throw new DuplicateResourceException("Email " + email + " already exists");
        user.setPassword(encoder.encode(request.password()));
        return mapper.toRegisterResponse(repository.save(user));
    }
    //
    public AuthResponse verify(LoginRequest request) throws AuthenticationException
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(),request.password()));
        // If authenticate fails, it throws AuthenticationException
        String token = jwtService.generateToken(authentication.getName());
        // Generates JWT using the AUTHENTICATED Email
        return new AuthResponse(
                null,
                null,
                null,
                token,
                "Bearer",
                expirationTime);
   }
}