package kr.co.sist.e_learning.admin.signup;

import java.util.List;


public interface AdminSignupDAO {
    void insertVerificationCode(EmailVerificationDTO dto);
    EmailVerificationDTO selectValidVerification(String email, String code);
    void markCodeVerified(String verificationSeq);
    void insertSignupRequest(AdminSignupDTO dto);
    void insertSignupPermissions(String requestId, List<String> roles);
    int isEmailDuplicated(String email);
    
}
