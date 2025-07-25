package kr.co.sist.e_learning.user.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String secretKey = "mySuperSecretKeyThatIsAtLeast32BytesLong!";
    private final long accessTokenValidity = 1000 * 60 * 30; // 30분
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7일

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId, Long userSeq) {
        return createToken(userId, userSeq, accessTokenValidity);
    }

    public String createRefreshToken() {
        return java.util.UUID.randomUUID().toString();
    }

    private String createToken(String userId, Long userSeq, long validityInMillis) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("userSeq", userSeq); // userSeq를 커스텀 클레임으로 추가
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long getUserSeq(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userSeq", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 만료된 토큰은 여기서 다시 던져서 JwtAuthenticationFilter에서 처리하도록 함
            throw e; 
        } catch (JwtException | IllegalArgumentException e) {
            // 다른 JWT 관련 예외나 잘못된 인자 예외는 유효하지 않은 토큰으로 간주
            return false;
        }
    }

}
