package kr.co.sist.e_learning.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AdminLogService logService;

    @Autowired(required = false)
    private HttpServletRequest request;

    @AfterReturning(pointcut = "@annotation(loggable)")
    public void logActivity(JoinPoint joinPoint, Loggable loggable) {
        String actionType = loggable.actionType();
        String adminId = "unknown";
        String targetId = null;
        String details = "";

        // 1. Get Admin ID from Session
        if (request != null) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("admin") != null) {
                // Assuming the admin's ID is stored in the session attribute "admin"
                adminId = (String) session.getAttribute("admin");
            }
            details = "IP: " + request.getRemoteAddr();
        }

        // 2. (Optional) Get Target ID from method arguments
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            // Example: if the first argument is the target object (e.g., a DTO)
            // This part will need to be customized based on your method signatures
            if (args[0] instanceof String) {
                targetId = (String) args[0];
            }
            // Add more conditions here for other types of DTOs
        }

        // 3. Create and Save the Log
        AdminLogDTO logDTO = new AdminLogDTO();
        logDTO.setAdminId(adminId);
        logDTO.setActionType(actionType);
        logDTO.setTargetId(targetId);
        logDTO.setDetails(details);

        logService.addLog(logDTO);
    }
}
