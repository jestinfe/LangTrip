package kr.co.sist.e_learning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;

import kr.co.sist.e_learning.user.auth.AuthService;
import kr.co.sist.e_learning.user.auth.JwtAuthUtils;
import kr.co.sist.e_learning.user.auth.JwtAuthenticationFilter;
import kr.co.sist.e_learning.user.auth.JwtTokenProvider;

@Configuration
@Order(2)
public class UserSecurityConfig {

    @Autowired
    private CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthUtils jwtAuthUtils;

    @Autowired
    private AuthService authService;


    @Bean
    public SecurityFilterChain userSecurity(HttpSecurity http) throws Exception {
        http
            .securityMatcher(request -> !request.getServletPath().startsWith("/admin")) // Exclude admin paths
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers(
            			    "/css/**", "/js/**", "/images/**", "/upload/images/**", // 정적 리소스 및 fallback
            			    "/userprofile/**",  // 프로필 이미지
            			    "/community/**",    // 커뮤니티 첨부 이미지
            			    "/courseImg/**",    // 강의 썸네일 이미지
            			    "/quiz/**",         // 퀴즈 이미지
            			    "/", "/login", "/signup", "/social_signup",
            			    "/forgot-username", "/forgot-password", "/reset-password",
            			    "/user/logout", "/user/login/**",

            			    // API
            			    "/api/auth/email/**",
            			    "/api/auth/nickname/check",
            			    "/api/auth/nickname/find",
            			    "/api/auth/password/**",
            			    "/api/auth/signup",
            			    "/api/auth/socialSignup",
            			    "/api/auth/login/**",
            			    "/api/auth/token/refresh"
            			).permitAll()
                .anyRequest().authenticated()
            )
      

            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .successHandler(customOAuth2AuthenticationSuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider, jwtAuthUtils, authService), UsernamePasswordAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtProvider,
                                                           JwtAuthUtils jwtAuthUtils,
                                                           AuthService authService) {
        return new JwtAuthenticationFilter(jwtProvider, jwtAuthUtils, authService);
    }
    
}
