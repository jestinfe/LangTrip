package kr.co.sist.e_learning.admin.auth;

import java.util.List;

public interface AdminSignupDAO {
    int insertAdmin(AdminSignupDTO dto);
    void insertPermission(String adminSeq, String permission);
    
    public void insertVerificationCode(EmailVerificationDTO dto);
    public EmailVerificationDTO selectValidVerification(String email, String code);
    public void markCodeVerified(String verificationSeq);

}

