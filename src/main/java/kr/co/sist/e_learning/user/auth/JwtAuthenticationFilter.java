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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthUtils jwtAuthUtils;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, JwtAuthUtils jwtAuthUtils, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthUtils = jwtAuthUtils;
        this.authService = authService;
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
                    logger.info("JwtAuthenticationFilter: Access Token valid. Setting authentication for userSeq: {}", userSeq);
                } else {
                    logger.warn("JwtAuthenticationFilter: Invalid (non-expired) Access Token for URI: {}. Attempting refresh.", request.getRequestURI());
                    // Access token invalid, try to refresh
                    userSeq = authService.reissueAccessToken(request, response);
                    logger.info("JwtAuthenticationFilter: Access Token refreshed. New userSeq: {}", userSeq);
                }
            } else {
                logger.debug("JwtAuthenticationFilter: No Access Token found in cookies for URI: {}. Attempting refresh.", request.getRequestURI());
                // No access token, try to refresh
                userSeq = authService.reissueAccessToken(request, response);
                logger.info("JwtAuthenticationFilter: Access Token refreshed. New userSeq: {}", userSeq);
            }

            if (userSeq != null) {
                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
            }

        } catch (ExpiredJwtException e) {
            logger.warn("JwtAuthenticationFilter: Token expired for URI: {}. Clearing tokens.", request.getRequestURI());
            jwtAuthUtils.deleteAccessTokenCookie(response);
            jwtAuthUtils.deleteRefreshTokenCookie(response);
            // No need to call authService.logout(refreshToken) here, as it's handled by the client or other logout mechanisms.
            // The goal here is to clear invalid tokens and let Spring Security handle the unauthenticated state.
        } catch (Exception e) {
            logger.error("JwtAuthenticationFilter: Error during token processing for URI: {}: {}", request.getRequestURI(), e.getMessage(), e);
            // In case of any other error, clear tokens to prevent infinite loops or bad state
            jwtAuthUtils.deleteAccessTokenCookie(response);
            jwtAuthUtils.deleteRefreshTokenCookie(response);
        }

        filterChain.doFilter(request, response);
    }
}