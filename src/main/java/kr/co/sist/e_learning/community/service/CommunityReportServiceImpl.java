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
                           Integer reasonChk,
                           String reasonText) {


        boolean alreadyReported = reportDAO.hasReportedToday(postId2, reporterId);

        if (alreadyReported) {
            throw new IllegalStateException("ALREADY_REPORTED");
        }

        CommunityReportDTO dto = new CommunityReportDTO();
        dto.setPostId2(postId2);
        dto.setReporterId(reporterId);
        dto.setReasonText(reasonText);
        
        reportDAO.insertReport(dto);
        Long newReportId = dto.getReportId();

        reasonDAO.insertReason(newReportId, reasonChk);
    }
}


