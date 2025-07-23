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
            .securityMatcher("/admin/**") // /admin/** 에만 적용
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/login", "/admin/login", "/admin/signup").permitAll()
                .anyRequest().authenticated() // Temporarily permit all requests under /admin/** for debugging
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("adminId")
                .passwordParameter("adminPw")
                .successHandler(customAdminAuthenticationSuccessHandler)
                .failureHandler(customAdminAuthenticationFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessHandler(customAdminLogoutSuccessHandler)
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
        provider.setUserDetailsService(customAdminDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}

