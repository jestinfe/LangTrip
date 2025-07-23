package kr.co.sist.e_learning.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DebugSecurityContextFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(DebugSecurityContextFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().startsWith("/admin")) {
            logger.debug("DEBUG_FILTER: Request to admin path: {}", request.getServletPath());
            logger.debug("DEBUG_FILTER: SecurityContextHolder Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
            logger.debug("DEBUG_FILTER: Session ID: {}", request.getSession(false) != null ? request.getSession(false).getId() : "No session");
        }
        filterChain.doFilter(request, response);
    }
}
