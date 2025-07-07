package kr.co.sist.e_learning.admin.signup_req;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AdminSignupRequestServiceImpl implements AdminSignupRequestService {

    @Autowired
    private AdminSignupRequestDAO dao;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<AdminSignupRequestDTO> listPendingRequests() {
        List<AdminSignupRequestDTO> list = dao.listPendingRequests();
        for (AdminSignupRequestDTO dto : list) {
            dto.setRoleCodes(dao.getRolesByRequestId(dto.getRequestId()));
        }
        return list;
    }

    @Override
    public AdminSignupRequestDTO getRequestById(String requestId) {
        AdminSignupRequestDTO dto = dao.getRequestById(requestId);
        if (dto != null) {
            dto.setRoleCodes(dao.getRolesByRequestId(requestId));
        }
        return dto;
    }

    @Override
    public void approveRequest(String requestId) {
        AdminSignupRequestDTO dto = dao.getRequestById(requestId);
        if (dto == null) {
            throw new IllegalArgumentException("가입 요청 정보가 존재하지 않습니다: " + requestId);
        }
        System.out.println("[승인 요청] adminId = " + dto.getAdminId());
        System.out.println("[승인 요청] email = " + dto.getEmail());

        List<String> roles = dao.getRolesByRequestId(requestId);

        // 1. 관리자 계정 등록 (이메일을 ID로 사용)
        dao.insertAdmin(dto);

        // 2. 권한 등록
        dao.insertRoles(dto.getAdminId(), roles);

        // 3. 승인 상태 변경
        dao.approveRequest(dto);

        // 4. 요청 삭제
        dao.deleteRequest(requestId);

        // 5. 승인 메일 전송
        sendApprovalEmail(dto.getEmail(), dto.getAdminName());
    }

    @Override
    public void rejectRequest(String requestId) {
        AdminSignupRequestDTO dto = dao.getRequestById(requestId);
        if (dto == null) {
            throw new IllegalArgumentException("가입 요청 정보가 존재하지 않습니다: " + requestId);
        }

        // 상태를 거절로 변경
        dao.rejectRequest(requestId);
    }

    private void sendApprovalEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("[e러닝 관리자 가입 승인 완료]");
            message.setText("안녕하세요, " + name + "님.\n\n요청하신 관리자 가입이 승인되었습니다.\n지금 바로 로그인하실 수 있습니다.");
            mailSender.send(message);
        } catch (Exception e) {
            // 로그는 추후 LogService로 넘겨도 됨
            System.err.println("이메일 전송 실패: " + e.getMessage());
        }
    }
}
