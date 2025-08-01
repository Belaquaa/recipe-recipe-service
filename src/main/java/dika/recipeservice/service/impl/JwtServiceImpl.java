package dika.recipeservice.service.impl;


import dika.recipeservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expirationMs}")
    private Long Ms;

    public boolean isTokenValid(String token, String username) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return ((claims.getSubject()).equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UUID extractExternalId(String token) {
        String externalIdStr = extractClaim(token, claims -> claims.get("externalId", String.class));
        return externalIdStr != null ? UUID.fromString(externalIdStr) : null;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Collection<GrantedAuthority> extractAuthorities(String token) {
        try {
            List<String> roles = extractRoles(token);
            return roles.stream()
                    .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);

        var rolesList = (List<LinkedHashMap<String, Object>>) claims.get("roles");

        return rolesList.stream()
                .map(roleMap -> (String) roleMap.get("role"))
                .toList();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
