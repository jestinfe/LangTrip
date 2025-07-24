package kr.co.sist.e_learning.mypage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

import  kr.co.sist.e_learning.user.*;
import kr.co.sist.e_learning.adBanner.AdBannerEntity;
import kr.co.sist.e_learning.adBanner.AdBannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private long getOrInitUserSeq(Authentication auth) {
        Object raw = auth.getPrincipal();
        if (raw instanceof Long userSeq) {
            return userSeq;
        }
        return 1001L;
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

    /**
     * 대시보드 fragment
     */
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

        // 인증된 사용자 정보 가져오기
        Object principal = auth.getPrincipal();
        long userSeq = (Long) principal;  // userSeq 가져오기

        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "파일이 없습니다.");
            return result;
        }

        try {
            // 확장자 검사
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
            if (!List.of("png", "jpg", "jpeg", "gif").contains(ext)) {
                result.put("success", false);
                result.put("message", "허용되지 않은 확장자입니다.");
                return result;
            }

            // 저장할 디렉토리 경로 설정 (uploadDirRoot 경로에 community 폴더 추가)
            File dir = new File(uploadDirRoot + "/userprofile");  // ${file.upload-dir.profile} 경로 사용
            if (!dir.exists()) {
                boolean dirCreated = dir.mkdirs();  // 디렉토리 생성
                if (!dirCreated) {
                    result.put("success", false);
                    result.put("message", "디렉토리 생성 실패");
                    return result;
                }
            }

            // 기존 이미지 삭제
            String oldPath = mpSV.selectProfilePath(userSeq);
            if (oldPath != null && oldPath.startsWith("/userprofile/")) {
                String oldFileName = oldPath.replace("/userprofile/", "");
                File oldFile = new File(dir, oldFileName);
                if (oldFile.exists()) {
                    oldFile.delete();  // 기존 파일 삭제
                }
            }

            // 새 파일명 생성
            String uuid = UUID.randomUUID().toString();
            String newFileName = uuid + "_" + userSeq + "." + ext;
            File destFile = new File(dir, newFileName);

            // 파일 서버에 저장
            file.transferTo(destFile);

            // DB 경로 업데이트 (웹 경로로 설정)
            String webPath = "/userprofile/" + newFileName;  // 상대 경로 (웹 접근용)
            mpSV.updateUserProfile(userSeq, webPath);

            // 결과 반환
            result.put("success", true);
            result.put("newPath", webPath);  // {"newPath": "/userprofile/filename.jpg"}
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "업로드 중 오류 발생");
        }

        return result;
    }


    @GetMapping("/instroductor_course")
    public String instructorCoursePage() {
        return "mypage/instroductor_course";
    }

    @GetMapping("/user_course")
    public String userCoursePage() {
        return "mypage/user_course";
    }

    @GetMapping("/reset-password")
    public String resetPassword() {
        return "user/login/reset-password";
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

    @GetMapping("/wallet")
    public String accountPage(Model model, Authentication auth) {
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
