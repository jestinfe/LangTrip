package kr.co.sist.e_learning.mypage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.co.sist.e_learning.adBanner.AdBannerEntity;
import kr.co.sist.e_learning.adBanner.AdBannerService;
import kr.co.sist.e_learning.mypage.UserAccountDTO;
import kr.co.sist.e_learning.usercourse.UserCourseDTO;
import kr.co.sist.e_learning.usercourse.UserCourseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    // ─── 서비스 주입 ─────────────────────────
    private final AdBannerService abSV;
    private final MyPageService mpSV;
    private final FundingService fdSV;

    @Autowired
    public MyPageController(AdBannerService abSV,
                            MyPageService mpSV,
                            FundingService fdSV) {
        this.abSV = abSV;
        this.mpSV = mpSV;
        this.fdSV = fdSV;
    }

    @Value("${file.upload-dir.root}")
    private String uploadDirRoot;

    @Value("${upload.path.profile}")
    private String uploadPathWeb;
    @Autowired
    private UserCourseService ucs;
    
    private long getOrInitUserSeq(Authentication auth) {
        Object raw = auth.getPrincipal();
        if (raw instanceof Long userSeq) {
            return userSeq;
        }
        return 0L;
    }

    /**
     * 마이페이지 메인 진입
     * - tab, 광고 배너, fragment 데이터 초기화
     */
    @GetMapping
    public String mypage(@RequestParam(value = "tab", defaultValue = "dashboard") String tab,
                         Authentication auth,
                         Model model) {
        long userSeq = getOrInitUserSeq(auth);

        // 기존 tab 속성
        model.addAttribute("tab", tab);

        // ▶ 광고 배너 목록 추가
        List<AdBannerEntity> top5Banners = abSV.getTop5Banners();
        List<AdBannerEntity> next5Banners = abSV.getNext5Banners();
        
        model.addAttribute("bannerList1", top5Banners);
        model.addAttribute("bannerList2", next5Banners);

        // fragment별 데이터
        switch (tab) {
            case "my_info":
                model.addAttribute("myData", mpSV.getUserInfo(userSeq));
                break;
            case "lecture_history":
                model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
                model.addAttribute("myLectureList", mpSV.selectMyLectures(userSeq));
                break;
            case "subscriptions":
                model.addAttribute("subscriptionList", mpSV.getSubscriptions(userSeq));
                break;
            case "wallet":
                model.addAttribute("accountInfo", fdSV.getAccountInfo(userSeq));
                break;
            case "payments":
                model.addAttribute("paymentList", mpSV.getPaymentHistory(userSeq));
                break;
            case "refund_history":
                model.addAttribute("refundList", mpSV.getRefundHistory(userSeq));
                break;
            case "donation":
                model.addAttribute("fundingList", fdSV.getUserFundings(userSeq));
                model.addAttribute("donationType", "given");
                break;
            case "dashboard":
            default:
                model.addAttribute("myData", mpSV.getMyPageData(userSeq));
                break;
        }

        // 메인 뷰
        return "mypage/mypage_main";
    }

    
    @GetMapping("/instroductor_course")
    public String instructorCoursePage() {
        return "mypage/instroductor_course"; // fragment화된 HTML 파일 경로
    }
    
    @GetMapping("/user_course")
    public String showUserCourse(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            Model model
            ) {

        long userSeq = getOrInitUserSeq(authentication);
        System.out.println("user_course 진입");
        System.out.println("USERSEQ : "+ userSeq);
        System.err.println("로그야 좀 떠라 시발");
        System.out.println("페이지, 리미트" + page+" " + limit);
        int offset = (page - 1) * limit;
        Map<String, Object> param = new HashMap<>();
        param.put("userSeq", userSeq);
        param.put("offset", offset);
        param.put("limit", limit);
//
        List<UserCourseDTO> courseList = ucs.searchUserCourseByPage(param);
        for(UserCourseDTO uDTO : courseList) {
        	System.out.println(uDTO.toString());
        }
//        
        int totalCount = ucs.searchUserCourseCount(userSeq);

        model.addAttribute("courses", courseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);

        return "mypage/user_course"; 
    }
    
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("myData", mpSV.getMyPageData(userSeq));
        return "mypage/dashboard";
    }

    /**
     * 강의 이력 fragment
     */
    @GetMapping("/lecture_history")
    public String lectureHistory(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
        model.addAttribute("myLectureList", mpSV.selectMyLectures(userSeq));
        return "mypage/lecture_history";
    }

    /** 내 정보 fragment **/
    @GetMapping("/my_info")
    public String myInfo(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("myData", mpSV.getUserInfo(userSeq));
        return "mypage/my_info";
    }
    
    /**
     * 프로필 업로드
     */
    @PostMapping("/upload_profile")
    @ResponseBody
    public Map<String, Object> uploadProfile(@RequestParam("file") MultipartFile file,
                                             Authentication auth) {
        Map<String, Object> result = new HashMap<>();
        Object raw = auth.getPrincipal();
        if (!(raw instanceof Long userSeq) || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "사용자 정보 또는 파일이 없습니다.");
            return result;
        }
        try {
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
            if (!List.of("png", "jpg", "jpeg", "gif").contains(ext)) {
                result.put("success", false);
                result.put("message", "이미지 파일만 업로드 가능합니다.");
                return result;
            }
            String filename = "profile_" + userSeq + "." + ext;
            File uploadFolder = new File(uploadDirRoot + "/userprofile");
            if (!uploadFolder.exists()) uploadFolder.mkdirs();
            File dest = new File(uploadFolder, filename);
            file.transferTo(dest);
            String dbPath = uploadPathWeb + "/" + filename;
            mpSV.updateUserProfile(userSeq, dbPath);
            result.put("success", true);
            result.put("newPath", dbPath);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "업로드 중 오류 발생");
        }
        return result;
    }

    /**
     * 프로필 삭제
     */
    @PostMapping("/delete_profile")
    @ResponseBody
    public Map<String, Object> deleteProfile(Authentication auth) {
        Map<String, Object> result = new HashMap<>();
        Object raw = auth.getPrincipal();
        if (!(raw instanceof Long userSeq)) {
            result.put("success", false);
            result.put("message", "사용자 정보 없음");
            return result;
        }
        try {
            String profilePath = mpSV.selectProfilePath(userSeq);
            if (profilePath != null && !profilePath.isBlank()) {
                File file = new File(uploadDirRoot + "/userprofile", profilePath.replace("/userprofile/", ""));
                if (file.exists()) file.delete();
            }
            mpSV.updateUserProfile(userSeq, null);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "삭제 중 오류 발생");
        }
        return result;
    }


    @GetMapping("/user_course")
    public String userCoursePage() {
        return "mypage/user_course";
    }

    @GetMapping("/reset_password")
    public String resetPassword() {
        return "mypage/reset_password";
    }

    @GetMapping("/link_account")
    public String linkAccount(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        UserAccountDTO accountInfo = mpSV.getUserAccount(userSeq);
        if (accountInfo != null) {
            model.addAttribute("linked", true);
            model.addAttribute("bank", accountInfo.getBankCode());
            model.addAttribute("maskedAccount", maskAccountNumber(accountInfo.getAccountNum()));
            model.addAttribute("holderName", accountInfo.getHolderName());
            model.addAttribute("linkedAt", accountInfo.getCreatedAt());
        } else {
            model.addAttribute("linked", false);
        }
        return "mypage/link_account";
    }

    @PostMapping("/link-account")
    public String linkAccountProcess(@RequestParam("bank") String bank,
                                     @RequestParam("account") String account,
                                     @RequestParam("owner") String owner,
                                     Authentication auth,
                                     Model model) {
        long userSeq = getOrInitUserSeq(auth);
        logger.info("Attempting to link account for userSeq: {}", userSeq);
        UserAccountDTO dto = new UserAccountDTO();
        dto.setUserSeq(userSeq);
        dto.setBankCode(bank);
        dto.setAccountNum(account);
        dto.setHolderName(owner);

        if (mpSV.linkUserAccount(dto)) {
            model.addAttribute("message", "계좌가 성공적으로 연동되었습니다.");
        } else {
            model.addAttribute("message", "계좌 연동에 실패했습니다.");
        }
        return "redirect:/mypage/link_account";
    }

    @PostMapping("/unlink-account")
    public String unlinkAccountProcess(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        if (mpSV.unlinkUserAccount(userSeq)) {
            model.addAttribute("message", "계좌 연동이 해제되었습니다.");
        } else {
            model.addAttribute("message", "계좌 연동 해제에 실패했습니다.");
        }
        return "redirect:/mypage/link_account";
    }

    private String maskAccountNumber(String acct) {
        if (acct == null || acct.length() < 4) return acct;
        return acct.substring(0, acct.length() - 4).replaceAll(".", "*") + acct.substring(acct.length() - 4);
    }

    @GetMapping("/leave")
    public String leave() {
        return "mypage/leave";
    }

    @GetMapping("/subscriptions")
    public String subscriptionPage(Authentication auth, Model model) {
        long userSeq = getOrInitUserSeq(auth);
        List<SubscriptionDTO> subscriptions = mpSV.getSubscriptions(userSeq);
        model.addAttribute("subscriptionList", subscriptions);
        return "mypage/subscriptions";
    }


    @GetMapping("/wallet")
    public String accountPage(Model model, Authentication auth) {
    	System.out.println("지갑 공간");
        long userSeq = getOrInitUserSeq(auth);
        FundingDTO accountInfo = fdSV.getAccountInfo(userSeq);
        model.addAttribute("accountInfo", accountInfo);
        return "mypage/wallet";
    }

    @GetMapping("/settlement")
    public String settlementPage(Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        // 정산 가능 금액 조회
        FundingDTO fundingInfo = fdSV.getAccountInfo(userSeq);
        double availableSettlementAmount = (fundingInfo != null) ? fundingInfo.getDonationAvailable() : 0;
        model.addAttribute("availableSettlementAmount", availableSettlementAmount);

        // 진행 중인 정산 확인
        SettlementRequestDTO pendingSettlement = mpSV.getPendingSettlementRequest(userSeq);
        model.addAttribute("hasPendingSettlement", pendingSettlement != null);

        // 정산 내역 조회
        List<SettlementRequestDTO> settlementHistory = mpSV.getSettlementHistory(userSeq);
        model.addAttribute("settlementHistory", settlementHistory);

        return "mypage/settlement";
    }

    @PostMapping("/settlement/request")
    @ResponseBody
    public Map<String, Object> requestSettlement(Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        long userSeq = getOrInitUserSeq(auth);

        if (userSeq == 0) {
            response.put("success", false);
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
            return response;
        }

        // 계좌 등록 여부 확인
        UserAccountDTO userAccount = mpSV.getUserAccount(userSeq);
        if (userAccount == null) {
            response.put("success", false);
            response.put("message", "정산 신청을 위해 계좌 등록이 필요합니다.");
            response.put("redirect", "/mypage/link_account");
            return response;
        }

        try {
            boolean result = mpSV.requestSettlement(userSeq);
            if (result) {
                response.put("success", true);
                response.put("message", "정산 신청이 완료되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "정산 신청에 실패했습니다. 진행 중인 정산이 있거나 정산 가능 금액이 부족합니다.");
            }
        } catch (Exception e) {
            logger.error("Error requesting settlement for userSeq: {}", userSeq, e);
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
        }
        return response;
    }

    @GetMapping("/settlement/detail")
    public String settlementDetailPage(@RequestParam("requestSeq") long requestSeq, Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        SettlementRequestDTO settlement = mpSV.getSettlementDetail(requestSeq);

        // 해당 정산 요청이 현재 사용자의 것인지 확인
        if (settlement == null || settlement.getUserSeq() != userSeq) {
            // 에러 처리 또는 목록 페이지로 리다이렉트
            return "redirect:/mypage/settlement";
        }

        model.addAttribute("settlement", settlement);
        return "mypage/settlement_detail";
    }
    
    @GetMapping("/payments")
    public String paymentsPage(Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        List<PaymentsDTO> paymentList = mpSV.getPaymentHistory(userSeq);
        model.addAttribute("paymentList", paymentList);
        return "mypage/payments";
    }

    
    @PostMapping("/refund/request")
    @ResponseBody
    public Map<String, Object> requestRefund(@RequestBody RefundRequestDTO refundRequestDTO,
                                             Authentication auth) {
        Map<String, Object> response = new HashMap<>();
        long userSeq = getOrInitUserSeq(auth);
        if (userSeq == 0) {
            response.put("success", false);
            response.put("message", "사용자 정보 없음");
            return response;
        }
        UserAccountDTO userAccount = mpSV.getUserAccount(userSeq);
        if (userAccount == null) {
            response.put("success", false);
            response.put("message", "계좌 등록 필요");
            response.put("redirect", "/mypage/link_account");
            return response;
        }
        try {
            boolean result = mpSV.requestRefund(userSeq, refundRequestDTO);
            response.put("success", result);
            response.put("message", result ? "환불 신청 접수" : "환불 신청 실패");
        } catch (Exception e) {
            logger.error("Refund error for userSeq {}", userSeq, e);
            response.put("success", false);
            response.put("message", "서버 오류");
        }
        return response;
    }

    @GetMapping("/refund/refundable-payments")
    @ResponseBody
    public List<PaymentsDTO> getRefundablePayments(Authentication auth) {
        return mpSV.getRefundablePayments(getOrInitUserSeq(auth));
    }

    @GetMapping("/refund/history")
    public String refundHistoryPage(Model model, Authentication auth) {
        model.addAttribute("refundList", mpSV.getRefundHistory(getOrInitUserSeq(auth)));
        return "mypage/refund_history";
    }

    @GetMapping("/donation")
    public String fundingPage(Model model, Authentication auth) {
        long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("fundingList", fdSV.getUserFundings(userSeq));
        model.addAttribute("donationType", "given");
        return "mypage/donation";
    }

    @GetMapping("/user_header")
    public String userHeader() {
        return "mypage/user_header";
    }

    @GetMapping("/sidebar")
    public String sidebarPage() {
        return "mypage/sidebar";
    }
}
