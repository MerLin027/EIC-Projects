package com.eic.product_catalogue_api.auth.repository;

import com.eic.product_catalogue_api.auth.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MyUser,Long>
{
    Optional<MyUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
