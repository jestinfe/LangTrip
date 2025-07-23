package kr.co.sist.e_learning.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

import kr.co.sist.e_learning.admin.auth.CustomAdminDetailsService;

@Configuration
@Order(1)
public class AdminSecurityConfig {

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;
    @Autowired
    private CustomAdminAuthenticationSuccessHandler customAdminAuthenticationSuccessHandler;
    @Autowired
    private CustomAdminAuthenticationFailureHandler customAdminAuthenticationFailureHandler;
    @Autowired
    private CustomAdminLogoutSuccessHandler customAdminLogoutSuccessHandler;

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
                .requestMatchers("/admin/login", "/admin/login", "/admin/signup").permitAll()
                .anyRequest().authenticated() // Temporarily permit all requests under /admin/** for debugging
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
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
            )
            .securityContext(securityContext -> securityContext.securityContextRepository(securityContextRepository()))
            .addFilterBefore(new DebugSecurityContextFilter(), SecurityContextHolderFilter.class);

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
