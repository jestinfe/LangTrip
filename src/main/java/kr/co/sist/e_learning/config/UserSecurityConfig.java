package kr.co.sist.e_learning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import kr.co.sist.e_learning.user.auth.AuthService;
import kr.co.sist.e_learning.user.auth.JwtAuthUtils;
import kr.co.sist.e_learning.user.auth.JwtAuthenticationFilter;
import kr.co.sist.e_learning.user.auth.JwtTokenProvider;

@Configuration
@Order(2)
public class UserSecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/css/**"))
                .requestMatchers(new AntPathRequestMatcher("/js/**"))
                .requestMatchers(new AntPathRequestMatcher("/images/**"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"));
    }


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
            			        "/css/**", "/js/**", "/images/**", "/", 
            			        "/login", "/signup", "/social_signup",
            			        "/forgot-username", "/forgot-password", "/reset-password",
            			        "/user/logout", "/user/login/**",
            			        
            			        // 🔐 로그인 없이 접근 가능한 API 경로 추가
            			        "/api/auth/email/**",
                                "/api/auth/nickname/check",
                                "/api/auth/nickname/find",
                                "/api/auth/password/forgot",
                                "/api/auth/signup",
                                "/api/auth/socialSignup",
                                "/api/auth/login/**",
                                "/api/auth/status",
                                
            			        "/api/auth/token/refresh",
            			        "/courses/**", "/csj/**", "/support/**","/courseImg/**"
            			        
            				 ).permitAll()
                .requestMatchers("/api/auth/password/reset").authenticated()
                .anyRequest().authenticated()
            )
      

            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .successHandler(customOAuth2AuthenticationSuccessHandler)
            )
            .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider, jwtAuthUtils, authService), UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // X-Frame-Options 설정
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
