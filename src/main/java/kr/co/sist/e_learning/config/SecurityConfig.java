package kr.co.sist.e_learning.config;

import kr.co.sist.e_learning.user.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import kr.co.sist.e_learning.admin.auth.CustomAdminDetailsService;


@Configuration
public class SecurityConfig {

    @Autowired
    private CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;

    @Autowired
    private CustomAdminAuthenticationSuccessHandler customAdminAuthenticationSuccessHandler;

    @Autowired
    private CustomAdminLogoutSuccessHandler customAdminLogoutSuccessHandler;

    @Autowired
    private CustomAdminAuthenticationFailureHandler customAdminAuthenticationFailureHandler;

    @Autowired
    private CustomAdminDetailsService customAdminDetailsService;

    @Bean

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customAdminDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/login", "/admin/signup", "/admin/send-code", "/admin/verify-code", "/admin/check-id").permitAll()
                .requestMatchers("/admin/account/**").hasAnyRole("ACCOUNT", "SUPER")
                .requestMatchers("/admin/members/**").hasAnyRole("MEMBER", "SUPER")
                .requestMatchers("/admin/course/**").hasAnyRole("COURSE", "SUPER")
                .requestMatchers("/admin/subscriptions/**").hasAnyRole("SUBSCRIBE", "SUPER")
                .requestMatchers("/admin/payments/**").hasAnyRole("PAYMENT", "SUPER")
                .requestMatchers("/admin/donation/**").hasAnyRole("DONATION", "SUPER")
                .requestMatchers("/admin/community/**").hasAnyRole("COMMUNITY", "SUPER")
                .requestMatchers("/admin/report/**").hasAnyRole("REPORT", "SUPER")
                .requestMatchers("/admin/support/**").hasAnyRole("SUPPORT", "SUPER")
                .requestMatchers("/admin/log/**").hasAnyRole("LOG", "SUPER")
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/admin/login")
                .loginProcessingUrl("/admin/login")
                .usernameParameter("adminId")
                .passwordParameter("adminPw")
                .successHandler(customAdminAuthenticationSuccessHandler)
                .failureHandler(customAdminAuthenticationFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessHandler(customAdminLogoutSuccessHandler)
            )
            .oauth2Login(oauth2 -> oauth2

                .successHandler(customOAuth2AuthenticationSuccessHandler)
            );

        return http.build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtProvider,
                                                           JwtAuthUtils jwtAuthUtils,
                                                           AuthService authService) {
        return new JwtAuthenticationFilter(jwtProvider, jwtAuthUtils, authService);
    }

}
