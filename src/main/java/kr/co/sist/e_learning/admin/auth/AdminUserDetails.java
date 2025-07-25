package kr.co.sist.e_learning.admin.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import java.util.stream.Collectors;

public class AdminUserDetails implements UserDetails {


    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String status;

    public AdminUserDetails(AdminAuthDTO admin) {
        this.username = admin.getAdminId();
        this.password = admin.getAdminPw();
        this.authorities = admin.getRoles().stream()
                .map(role -> {
                   
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());
       
        this.status = admin.getStatus();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "Y".equals(status);
    }
}
