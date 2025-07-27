package kr.co.sist.e_learning.report;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService {
	@Autowired
	private ReportMapper reportMapper;
	
    // 1. 중복 신고 체크
    public boolean checkAlreadyReportedToday(String type, int contentId, int reporterId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("type", type); // "post" or "course"
        paramMap.put("contentId", contentId);
        paramMap.put("reporterId", reporterId);

        return reportMapper.existsTodayReport(paramMap);
    }

    // 2. 신고 등록 처리
    public void registerReport(String type, int contentId, ReportDTO dto) {
        // 신고 대상 ID 지정
        if (type.equals("post")) {
            dto.setPostId2(contentId);
        } else if (type.equals("course")) {
            dto.setCourseId(contentId);
        }

        dto.setActionStatus("미처리");
        dto.setReportedAt(new java.sql.Date(System.currentTimeMillis()));

        // 1) 신고 테이블에 등록
        System.out.println("🧪 insert 전 reportId: " + dto.getReportId());
        reportMapper.insertReport(dto);
        System.out.println("✅ insert 후 reportId: " + dto.getReportId());

        // 2) 선택한 신고 사유 체크박스 저장
        if (dto.getReporterCheckedReason() != null && !dto.getReporterCheckedReason().isEmpty()) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("reportId", dto.getReportId()); // selectKey로 채워질 것
            paramMap.put("reasonChkList", dto.getReporterCheckedReason());

            reportMapper.insertReportReasonUser(paramMap);
        }
    }
}
