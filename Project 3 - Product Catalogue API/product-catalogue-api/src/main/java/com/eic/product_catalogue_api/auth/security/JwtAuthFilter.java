package com.eic.product_catalogue_api.auth.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter
{
    public static final String JWT_ERROR_ATTRIBUTE = "jwt.error";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    //
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        //
        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            doFilter(request, response, filterChain);
            // Pass to the next filter in the SecurityFilterChain
            return;
        }
        //
        String token = authHeader.substring(7).trim();
        //
        if(token.isEmpty())
        {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "Token is missing");
            filterChain.doFilter(request, response);
            return;
        }
        //
        try
        {
            String username = jwtService.validateTokenAndGetUsername(token);
            // Validates the JWT and extracts the subject (username) from the payload
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            {
                UserDetails userDetails =  userDetailsService.loadUserByUsername(username);
                if(isAccountUsable(userDetails))
                {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken
                                    (userDetails.getUsername(), null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // This attaches incoming HTTP request metadata to the authToken
                    // like - IP Address and Session ID
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    // SecurityContext - Holds the current authentication information
                    // SecurityContextHolder - Provides access to the current security context
                    // Authentication - Represents the authenticated user and their authorities
                    // This marks the current user as logged in/authenticated
                }
                // If token is valid, build an authenticated object with UserDetails + its authorities
            }
        }
        catch(ExpiredJwtException e)
        {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "Token has expired");
        }
        catch(JwtException | IllegalArgumentException e)
        {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "Token is invalid");
        }
        catch(UsernameNotFoundException e)
        {
            request.setAttribute(JWT_ERROR_ATTRIBUTE, "User no longer exists");
        }
        filterChain.doFilter(request, response); // Pass to the next filter in the SecurityFilterChain
    }
    private boolean isAccountUsable(UserDetails userDetails) throws AuthenticationException
    {
        return userDetails.isEnabled()
                && userDetails.isAccountNonLocked()
                && userDetails.isAccountNonExpired()
                && userDetails.isCredentialsNonExpired();
        // We can use AccountStatusUserDetailsChecker object as well
        // accountChecker.check(userDetails);
        // throws if disabled, locked or expired
    }
}
