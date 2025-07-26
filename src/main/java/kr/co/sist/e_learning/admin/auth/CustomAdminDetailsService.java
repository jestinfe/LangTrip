package kr.co.sist.e_learning.admin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    
    @Autowired
    private AdminAuthDAO adminAuthDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminAuthDTO admin = adminAuthDAO.loginSelectAdminById(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found with id: " + username);
        }

        List<String> roles = adminAuthDAO.selectAdminRoles(admin.getAdminId());
      
        admin.setRoles(roles);

        return new AdminUserDetails(admin);
    }
    
    
    
    
}
