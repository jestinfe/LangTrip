package kr.co.sist.e_learning.admin.auth;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignupDTO {

    // 기본 입력값
	private String id;
    private String name;
    private String extension;
    private String email;
    private String emailCode;
    private String phone;
    private String password;      // 평문 비밀번호 (사용자 입력)
    private String confirmPassword; // 클라이언트 단에서도 받을 경우

    // 저장용
    private String hashedPassword; // DB에 저장될 암호화된 비밀번호

    // 권한 리스트
    private List<String> permissions;

    // DB 저장 후 받아올 값
    private String adminSeq;  // 관리자 PK (UUID 또는 시퀀스)
}
