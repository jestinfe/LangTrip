package kr.co.sist.e_learning.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String ajaxHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);

        if (isAjax) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.setContentType("application/json;charset=UTF-8");

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "접근 권한이 없습니다.");
            errorResponse.put("error", "Access Denied");

            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } else {
            // For non-AJAX requests, forward to a custom error page
            response.sendRedirect(request.getContextPath() + "/admin/login");
        }
    }
}
