package kr.co.sist.e_learning.admin.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminSignupServiceImpl implements AdminSignupService {

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public boolean registerAdmin(AdminSignupDTO dto) {
        try {
            AdminSignupDAO dao = sqlSession.getMapper(AdminSignupDAO.class);

            // 1. 이메일 인증코드 확인
            EmailVerificationDTO evDto = dao.selectValidVerification(dto.getEmail(), dto.getEmailCode());
            if (evDto == null) {
                return false; // 유효하지 않음
            }
            dao.markCodeVerified(evDto.getVerificationSeq()); // 인증코드 사용 처리

            // 2. 비밀번호 암호화
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPw = encoder.encode(dto.getPassword());
            dto.setPassword(hashedPw);

            // 3. 관리자 정보 저장
            dao.insertAdmin(dto); // dto 내 adminSeq 자동 설정 필요 (MyBatis selectKey 등 사용)

            // 4. 권한 목록 저장
            List<String> permissions = dto.getPermissions();
            if (permissions != null && !permissions.isEmpty()) {
                for (String perm : permissions) {
                    dao.insertPermission(dto.getAdminSeq(), perm);
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace(); // 로깅 필요
            return false;
        }
    }

    @Override
    public void sendVerificationCode(String email) {
        String code = RandomStringUtils.randomNumeric(6);
        String uuid = UUID.randomUUID().toString();
        Timestamp expireAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(10));

        AdminSignupDAO dao = sqlSession.getMapper(AdminSignupDAO.class);

        EmailVerificationDTO dto = new EmailVerificationDTO();
        dto.setVerificationSeq(uuid);
        dto.setEmail(email);
        dto.setCode(code);
        dto.setExpiresAt(expireAt);
        dto.setStatus("SENT");

        dao.insertVerificationCode(dto);

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[이러닝] 관리자 인증코드 안내");
            helper.setText("<h2>인증코드: " + code + "</h2><p>10분 내 입력해주세요.</p>", true);
            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException("메일 발송 실패", e);
        }
    }


    @Override
    public boolean verifyEmailCode(String email, String code) {
        AdminSignupDAO dao = sqlSession.getMapper(AdminSignupDAO.class);
        EmailVerificationDTO dto = dao.selectValidVerification(email, code);
        if (dto == null) return false;
        dao.markCodeVerified(dto.getVerificationSeq());
        return true;
    }
}
