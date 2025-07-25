package kr.co.sist.e_learning.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AdminPageViewLoggingFilter extends OncePerRequestFilter {

    @Autowired
    private AdminLogService adminLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Only log GET requests for admin pages
        if (requestURI.startsWith("/admin") && "GET".equalsIgnoreCase(method)) {
            // Use a request attribute to ensure logging happens only once per logical request
            final String LOGGED_FLAG = "ADMIN_PAGE_VIEW_LOGGED_" + requestURI;
            if (request.getAttribute(LOGGED_FLAG) == null) {
                String adminId = getAdminId();
                String details = "IP: " + request.getRemoteAddr();

                AdminLogDTO logDTO = new AdminLogDTO();
                logDTO.setAdminId(adminId);
                logDTO.setActionType("PAGE_VIEW");
                logDTO.setTargetId(requestURI);
                logDTO.setDetails(details);

                adminLogService.addLog(logDTO);
                request.setAttribute(LOGGED_FLAG, true); // Set flag to prevent re-logging
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getAdminId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            return principal.toString();
        } else {
            return "anonymous";
        }
    }
}
