package kr.co.sist.e_learning.user.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class JwtAuthUtils {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthUtils(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 쿠키에서 Access Token 추출
     */
    public String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 쿠키에서 Refresh Token 추출
     */
    public String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 로그인 여부 판단
     */
    public boolean isLoggedIn(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);
        return token != null && jwtTokenProvider.validateToken(token);
    }

    /**
     * 유저 ID 추출
     */
    public String getUserIdFromToken(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return jwtTokenProvider.getUserId(token);
        }
        return null;
    }

    /**
     * Access Token 쿠키 세팅
     */
    public void setAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // 개발환경에서는 false
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60);
        response.addCookie(cookie);
    }


    /**
     * Refresh Token 쿠키 세팅
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(cookie);
    }
}
