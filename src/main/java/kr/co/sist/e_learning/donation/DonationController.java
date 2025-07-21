package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DonationController {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private DonationService donationService;

    @GetMapping("/user/donation")
    public String donationPage(@RequestParam("lectureNo") Long lectureNo, Authentication authentication, Model model) {
        // 1. 강의 정보 조회
        LectureDetailDTO lecture = lectureService.getLectureDetail(lectureNo);
        if (lecture == null) {
            return "error/404";
        }

        // 2. 현재 로그인한 사용자의 보유 마일리지 조회
        Long userSeq = (Long) authentication.getPrincipal();
        Long userMiles = donationService.getUserMiles(userSeq);

        // 3. 모델에 데이터 추가
        model.addAttribute("lecture", lecture);
        model.addAttribute("userMiles", userMiles);

        return "user/donation/donation";
    }

    @GetMapping("/donation/success")
    public String donationSuccess(@RequestParam String lectureTitle,
                                  @RequestParam String instructorName,
                                  @RequestParam int amount,
                                  @RequestParam(required = false) String message,
                                  Model model) {
        DonationDTO donation = new DonationDTO(lectureTitle, instructorName, amount, message);
        model.addAttribute("donation", donation);
        return "user/donation/donation_success";
    }
}
