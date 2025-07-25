package kr.co.sist.e_learning.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;

@Component
public class AdminLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminLoggingInterceptor.class);

    @Autowired
    private AdminLogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        logger.debug("AdminLoggingInterceptor: Processing request for URI: {}", requestURI);
        logger.debug("AdminLoggingInterceptor: INCLUDE_REQUEST_URI: {}", request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI));
        logger.debug("AdminLoggingInterceptor: FORWARD_REQUEST_URI: {}", request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));

        // This interceptor will no longer handle PAGE_VIEW logging

        return true;
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
