package kr.co.sist.e_learning.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.sist.e_learning.admin.account.AdminAccountUnifiedDTO;
import kr.co.sist.e_learning.admin.log.AdminLogDTO;
import kr.co.sist.e_learning.admin.log.AdminLogService;
import kr.co.sist.e_learning.admin.signup.AdminSignupDTO;
import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private AdminLogService logService;

    @AfterReturning(pointcut = "@annotation(loggable)", returning = "result")
    public void logActivity(JoinPoint joinPoint, Loggable loggable, Object result) {
        String adminId = getAdminId();
        String actionType = loggable.actionType();
        String targetId = getTargetId(joinPoint.getArgs(), actionType, adminId);
        String details = getRequestDetails();

        AdminLogDTO logDTO = new AdminLogDTO();
        logDTO.setAdminId(adminId);
        logDTO.setActionType(actionType);
        logDTO.setTargetId(targetId);
        logDTO.setDetails(details);

        logService.addLog(logDTO);
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

    private String getTargetId(Object[] args, String actionType, String adminId) {
        String targetId = null;
        if (args.length > 0) {
            if (args[0] instanceof String) {
                targetId = (String) args[0];
            } else if (args[0] instanceof AdminAccountUnifiedDTO) {
                targetId = ((AdminAccountUnifiedDTO) args[0]).getAdminId();
            } else if (args[0] instanceof AdminSignupDTO) {
                targetId = ((AdminSignupDTO) args[0]).getRequestId(); // Or getAdminId() depending on context
                if (targetId == null) {
                    targetId = ((AdminSignupDTO) args[0]).getAdminId();
                }
            }
            // Add more conditions for other DTOs as needed
        }

        // Special handling for logout to ensure targetId is adminId
        if ("ADMIN_LOGOUT".equals(actionType) && targetId == null) {
            targetId = adminId;
        }
        return targetId;
    }

    private String getRequestDetails() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return "IP: " + request.getRemoteAddr();
        }
        return "";
    }
}
