package com.eic.product_catalogue_api.auth.security;

import com.eic.product_catalogue_api.auth.entity.MyUser;
import com.eic.product_catalogue_api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        MyUser user = repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Username not found"));
        return new UserPrincipal(user);
    }
}
