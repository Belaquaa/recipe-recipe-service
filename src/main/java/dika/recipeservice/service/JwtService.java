package dika.recipeservice.service;


import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;


public interface JwtService {

    boolean isTokenValid(String token, String username);

    String extractUsername(String token);

    UUID extractExternalId(String token);

    Claims extractAllClaims(String token);

    Collection<GrantedAuthority> extractAuthorities(String token);
}

