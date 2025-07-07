package kr.co.sist.e_learning.admin.signup;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


@Transactional
@Service
public class AdminSignupServiceImpl implements AdminSignupService {

	@Autowired
	private JavaMailSender mailSender;

    @Autowired
    private AdminSignupDAO signupDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void sendVerificationCode(String email) {
        String code = generateRandomCode();
        EmailVerificationDTO dto = new EmailVerificationDTO();
        dto.setVerificationSeq(UUID.randomUUID().toString());
        dto.setEmail(email);
        dto.setCode(code);
        dto.setStatus("SENT");
        dto.setCreatedAt(Timestamp.from(Instant.now()));
        dto.setExpiresAt(Timestamp.from(Instant.now().plusSeconds(300))); // 5분 유효

        signupDAO.insertVerificationCode(dto);

        // TODO: 이메일 발송 구현 필요
        System.out.println("임시 인증 코드: " + code);
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[EduPro] 관리자 인증 코드");
            helper.setText("<h2>인증 코드: " + code + "</h2><p>5분 안에 입력하세요.</p>", true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("이메일 발송에 실패했습니다.");
        }
    }

    @Override
    public boolean verifyCode(String email, String code) {
        EmailVerificationDTO dto = signupDAO.selectValidVerification(email, code);
        if (dto != null) {
            signupDAO.markCodeVerified(dto.getVerificationSeq());
            return true;
        }
        return false;
    }

    @Override
    public void registerAdmin(AdminSignupDTO dto) {
    	    System.out.println("중복 확인 결과: " + signupDAO.isEmailDuplicated(dto.getEmail())); // ← 로그 찍어봐
        // 중복 체크
        if (signupDAO.isEmailDuplicated(dto.getEmail())> 0) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }
        dto.setRequestId(UUID.randomUUID().toString());
        
        System.out.println("A"+dto.getAdminId());
        dto.setAdminId(dto.getAdminId());

        // 비밀번호 암호화
        String hash = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hash);


        System.out.println("1. insertSignupRequest 시작");
        signupDAO.insertSignupRequest(dto);
        System.out.println("2. insertSignupRequest 완료");

        System.out.println("3. insertSignupPermissions 시작");
        signupDAO.insertSignupPermissions(dto.getRequestId(), dto.getPermissions());
        System.out.println("4. insertSignupPermissions 완료");
    }

    private String generateRandomCode() {
        SecureRandom rand = new SecureRandom();
        int code = 100000 + rand.nextInt(900000);
        return String.valueOf(code);
    }
}
