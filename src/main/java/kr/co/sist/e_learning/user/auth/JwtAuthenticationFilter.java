package kr.co.sist.e_learning.user.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthUtils jwtAuthUtils;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtAuthUtils jwtAuthUtils, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthUtils = jwtAuthUtils;
        this.authService = authService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().startsWith("/admin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtAuthUtils.extractTokenFromCookies(request);
        Long userSeq = null;

        try {
            if (accessToken != null) {
                if (jwtTokenProvider.validateToken(accessToken)) {
                    userSeq = jwtTokenProvider.getUserSeq(accessToken);
                    
                } else {
                    // Access token invalid, try to refresh
                    userSeq = authService.reissueAccessToken(request, response);
                }
            } else {
                // No access token, try to refresh
                userSeq = authService.reissueAccessToken(request, response);
            }

            if (userSeq != null) {
                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
            }

        } catch (ExpiredJwtException e) {
            jwtAuthUtils.deleteAccessTokenCookie(response);
            jwtAuthUtils.deleteRefreshTokenCookie(response);
            // No need to call authService.logout(refreshToken) here, as it's handled by the client or other logout mechanisms.
            // The goal here is to clear invalid tokens and let Spring Security handle the unauthenticated state.
        } catch (Exception e) {
            // In case of any other error, clear tokens to prevent infinite loops or bad state
            jwtAuthUtils.deleteAccessTokenCookie(response);
            jwtAuthUtils.deleteRefreshTokenCookie(response);
        }

        filterChain.doFilter(request, response);
    }
}