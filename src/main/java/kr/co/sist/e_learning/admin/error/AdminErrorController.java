package kr.co.sist.e_learning.admin.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            // Check if the original request was for an admin path
            String originalUri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            if (originalUri != null && originalUri.startsWith("/admin")) {
                // If it's an admin-related error, redirect to our custom admin access denied page
                try {
					response.sendRedirect(request.getContextPath() + "/admin/access-denied");
				} catch (IOException e) {
					e.printStackTrace();
				}
                return null;
            }
        }
        // For non-admin errors or if original URI is not available, let Spring Boot's default error handling take over
        return "forward:/error"; // This will typically render src/main/resources/templates/error.html
    }

    public String getErrorPath() {
        return null;
    }
}
