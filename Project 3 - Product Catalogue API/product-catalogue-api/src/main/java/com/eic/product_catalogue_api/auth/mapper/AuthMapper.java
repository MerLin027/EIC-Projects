package com.eic.product_catalogue_api.auth.mapper;

import com.eic.product_catalogue_api.auth.dto.AuthResponse;
import com.eic.product_catalogue_api.auth.dto.LoginRequest;
import com.eic.product_catalogue_api.auth.dto.RegisterRequest;
import com.eic.product_catalogue_api.auth.entity.MyUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper
{
    // RegisterRequest -> MyUser (used on /auth/register)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // the service hashes it
    @Mapping(target = "role", ignore = true) // default role - USER
    MyUser toEntity(RegisterRequest request);

    // LoginRequest -> MyUser
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true) // not in a login request
    @Mapping(target = "role", ignore = true)
    MyUser toEntity(LoginRequest request);

    // MyUser -> AuthResponse (used for the /auth/register reply)
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "tokenType", ignore = true)
    @Mapping(target = "expiresIn", ignore = true)
    AuthResponse toRegisterResponse(MyUser user);
}
