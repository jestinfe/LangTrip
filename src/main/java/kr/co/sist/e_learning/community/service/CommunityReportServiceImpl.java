package kr.co.sist.e_learning.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.community.dao.CommunityReportDAO;
import kr.co.sist.e_learning.community.dto.CommuRpModal;
import kr.co.sist.e_learning.community.dto.CommunityReportDTO;

@Service
public class CommunityReportServiceImpl implements CommunityReportService {

    @Autowired
    private CommunityReportDAO reportDAO;

    @Autowired
    private CommunityReportDAO reasonDAO;

    @Override
    public void reportPost(Long postId2,
                           Long reporterId,
                           Integer reasonChk,   // 추가
                           String reasonText) {
        System.out.println("신고 서비스 진입: postId2=" + postId2 +
                           ", reporterId=" + reporterId +
                           ", reasonChk=" + reasonChk +
                           ", reasonText=" + reasonText);

        // 1) 중복 신고
        if (reportDAO.hasReportedToday(postId2, reporterId)) {
            throw new IllegalStateException("ALREADY_REPORTED");
        }

        // 2) report 삽입
        CommunityReportDTO dto = new CommunityReportDTO();
        dto.setPostId2(postId2);
        dto.setReporterId(reporterId);
        dto.setReasonText(reasonText);
        reportDAO.insertReport(dto);
        Long newReportId = dto.getReportId();

        // 3) REPORT_REASON_USER 테이블에 사유 코드 삽입
        reasonDAO.insertReason(newReportId, reasonChk);
    }
}

