package kr.co.sist.e_learning.admin.signup;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignupDTO {
    private String requestId;          // 시퀀스에서 채번되므로 없어도 되지만 받아올 수도 있음
    private String adminId;
    private String name;              // ADMIN_NAME
    private String email;
    private String phone;
    private String password;          // PASSWORD_HASH
    private String dept;
    private String emailCode;         // 이메일 인증 코드
    private List<String> permissions; // 권한 코드 리스트
}
