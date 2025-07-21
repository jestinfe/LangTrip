package kr.co.sist.e_learning.user.auth;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String accessToken = jwtAuthUtils.extractTokenFromCookies(request);

            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Long userSeq = jwtTokenProvider.getUserSeq(accessToken);
                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
            } else {
                // 액세스 토큰이 없거나 유효하지 않으면 리프레시로 시도
                try {
                    Long userSeq = authService.reissueAccessToken(request, response);
                    SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(userSeq, null));
                } catch (Exception ex) {
                    // 재발급 실패해도 예외 던지지 않고 그냥 필터 통과시킴
                    jwtAuthUtils.deleteAccessTokenCookie(response);
                    jwtAuthUtils.deleteRefreshTokenCookie(response);
                    // 여기서 return하지 말고 그냥 다음 필터로 진행
                }
            }
        } catch (ExpiredJwtException e) {
            jwtAuthUtils.deleteAccessTokenCookie(response);
            // 만료된 경우에도 강제 응답 X
        } catch (Exception e) {
            jwtAuthUtils.deleteAccessTokenCookie(response);
        }

        filterChain.doFilter(request, response); // 무조건 다음 필터로 넘김
    }
}