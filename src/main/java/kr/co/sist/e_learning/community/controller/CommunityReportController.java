package kr.co.sist.e_learning.community.controller;

import jakarta.servlet.http.HttpSession;
import kr.co.sist.e_learning.community.dto.CommuRpModal;
import kr.co.sist.e_learning.community.dto.UsersssDTO;
import kr.co.sist.e_learning.community.service.CommunityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/csj/report")
public class CommunityReportController {

    @Autowired
    private CommunityReportService reportService;

    @PostMapping
    public ResponseEntity<String> reportPost(
            @RequestBody CommuRpModal req,
            HttpSession session) {


        UsersssDTO loginUser = (UsersssDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            loginUser = new UsersssDTO();
            loginUser.setUserSeq(400);
            loginUser.setNickname("임시사용자");
            session.setAttribute("loginUser", loginUser);
        }

        Long reporterId = loginUser.getUserSeq().longValue();

        try {
            reportService.reportPost(
                req.getPostId2(),
                reporterId,
                req.getReasonChk(),
                req.getReasonText()
            );
            return ResponseEntity.ok("신고가 정상 접수되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("하루에 한 번만 신고할 수 있습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신고 요청에 문제가 있습니다.");
        }
    }
}
