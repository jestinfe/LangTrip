package kr.co.sist.e_learning.admin.auth;

public interface AdminSignupService {
    boolean registerAdmin(AdminSignupDTO dto);
    void sendVerificationCode(String email);
    boolean verifyEmailCode(String email, String code);
}
