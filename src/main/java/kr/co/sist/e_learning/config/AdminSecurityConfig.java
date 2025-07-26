package kr.co.sist.e_learning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import lombok.extern.log4j.Log4j2;

import kr.co.sist.e_learning.admin.auth.CustomAdminDetailsService;
import kr.co.sist.e_learning.config.filter.AdminPageViewLoggingFilter;

@Configuration
@Order(1)
@Log4j2
public class AdminSecurityConfig {

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;
    @Autowired
    private CustomAdminAuthenticationSuccessHandler customAdminAuthenticationSuccessHandler;
    @Autowired
    private CustomAdminAuthenticationFailureHandler customAdminAuthenticationFailureHandler;
    @Autowired
    private CustomAdminLogoutSuccessHandler customAdminLogoutSuccessHandler;
    @Autowired
    private AdminAccessDeniedHandler adminAccessDeniedHandler;
    @Autowired
    private AdminAuthenticationEntryPoint adminAuthenticationEntryPoint;
    @Autowired
    private AdminPageViewLoggingFilter adminPageViewLoggingFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {
     
        http
            .securityMatcher("/admin/**") // /admin/** 경로에만 적용
            .authorizeHttpRequests(auth -> auth
                    // 로그인/가입 페이지는 모두에게 열어두고
                    .requestMatchers("/admin/login/**", "/admin/signup**").permitAll()

                    // 📌 클릭 카운트 API는 인증 없이 허용
                    .requestMatchers(HttpMethod.POST, "/admin/ad/click/**", "admin/send-code",
                    		"/admin/verify-code", "/admin/check-id").permitAll()

                    // 그 외 /admin/** 경로는 모두 인증 필요
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/admin/login") // 로그인 페이지 설정
                .loginProcessingUrl("/admin/login") // 로그인 처리 URL
                .usernameParameter("adminId") // 로그인 폼에서 사용하는 아이디 파라미터
                .passwordParameter("adminPw") // 로그인 폼에서 사용하는 패스워드 파라미터
                .successHandler(customAdminAuthenticationSuccessHandler) // 로그인 성공 처리
                .failureHandler(customAdminAuthenticationFailureHandler) // 로그인 실패 처리
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout") // 로그아웃 URL
                .logoutSuccessHandler(customAdminLogoutSuccessHandler) // 로그아웃 성공 처리
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(adminAccessDeniedHandler) // AccessDeniedHandler 설정
                .authenticationEntryPoint(adminAuthenticationEntryPoint) // AuthenticationEntryPoint 설정
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
            );
            

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider adminAuthProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customAdminDetailsService); // 사용자 상세 서비스 설정
        provider.setPasswordEncoder(passwordEncoder); // 비밀번호 인코더 설정
        return provider;
    }
}
