package org.example.taskmanager.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.enums.JwtType;
import org.example.taskmanager.entity.Jwt;
import org.example.taskmanager.repository.JwtDAO;
import org.example.taskmanager.util.JwtUtil;
import org.example.taskmanager.util.ProfileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final static Logger LOGGER = Logger.getLogger(JwtService.class.getName());

    @Value("${token.access.secret}")
    private String jwtSigningKey;
    @Value("${token.access.lifetime}")
    private Duration accessLifetime;
    @Value("${token.refresh.secret}")
    private String refreshJwtSigningKey;
    @Value("${token.refresh.lifetime}")
    private Duration refreshLifetime;

    private final ProfileUtil profileUtil;
    private final JwtUtil jwtUtil;
    private final JwtDAO jwtDAO;
    private final ModelMapper modelMapper;

    /**
     * Extract user login
     *
     * @param token token
     * @return profile login
     */
    public String extractUserLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRefreshUserLogin(String refreshToken) {
        return extractClaimRefresh(refreshToken, Claims::getSubject);
    }

    /**
     * Getting data from the token
     *
     * @param token           token
     * @param claimsResolvers function for extraction data
     * @param <T>             data type
     * @return data
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final var claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private <T> T extractClaimRefresh(String token, Function<Claims, T> claimsResolvers) {
        final var claims = extractAllClaimsRefresh(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Getting all data from token
     *
     * @param token token
     * @return data
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Extracting claims from token
     *
     * @param token token
     * @return token's claims
     */
    private Claims extractAllClaimsRefresh(String token) {
        return Jwts.parser()
                .setSigningKey(getRefreshSigKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    /**
     * Check token for expiration
     *
     * @param token token
     * @return true, if token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Getting time of expiration
     *
     * @param token token
     * @return time og expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getRefreshSigKey() {
        return getKey(refreshJwtSigningKey);
    }

    private Key getSigKey() {
        return getKey(jwtSigningKey);
    }

    private Key getKey(String key) {
        return Keys.hmacShaKeyFor(getByteKey(key));
    }

    private byte[] getByteKey(String key) {
        return Decoders.BASE64.decode(key);
    }

    /**
     * Deleting profile tokens from repository
     *
     * @param profileId profile identifier
     * @return deleting status
     */
    @Transactional
    public Boolean deleteProfileToken(UUID profileId) {
        profileUtil.tryToGetProfile(profileId);
        jwtDAO.deleteByProfileId(profileId);
        return jwtDAO.getByProfileId(profileId).isEmpty();
    }

    /**
     * Generating access token
     *
     * @param userDetails User details
     * @return jwt access token
     */
    public String generateAccessToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + accessLifetime.toMillis());
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getSigKey())
                .compact();
    }

    /**
     * Generating refresh token
     *
     * @param userDetails User details
     * @return jwt refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifetime.toMillis());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(getRefreshSigKey())
                .compact();
    }

    /**
     * Save token to token repository
     *
     * @param profileId    Profile numerical identifier
     * @param token        Token
     * @param jwtType      Token type
     * @return saved token
     */
    public String registerToken(UUID profileId, String token, JwtType jwtType) {
        var id = UUID.randomUUID();
        jwtDAO.save(new Jwt(
                id,
                profileId,
                token,
                jwtType
        ));

        return jwtUtil.getEntity(id).getToken();
    }

    /**
     * Validating refresh token
     *
     * @param refreshToken Token
     * @return true - if valid
     */
    public Boolean validateRefreshToken(String refreshToken) {
        return jwtUtil.isTokenValid(refreshToken, getRefreshSigKey());
    }

    /**
     * Validating access token
     *
     * @param token Token
     * @return true - if valid
     */
    public Boolean validateAccessToken(String token) {
        return jwtUtil.isTokenValid(token, getSigKey());
    }
}
