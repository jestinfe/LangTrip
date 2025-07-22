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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private MyPageService mpSV;
    
    @Autowired
    private FundingService fdSV;

    private long getOrInitUserSeq(Authentication auth) {
        Object raw = auth.getPrincipal();
        Long userSeq = null;
        if (raw instanceof Long) {
            userSeq = (Long) raw;
        }
        return userSeq;
    }



    /** 공통 진입점 (탭 파라미터 기반) */
    @GetMapping
    public String mypageMain(@RequestParam(value = "tab", required = false, defaultValue = "dashboard") String tab,
    		Authentication auth, Model model) {
    	long userSeq = getOrInitUserSeq(auth);

        switch (tab) {
            case "my_info":
                model.addAttribute("myData", mpSV.getUserInfo(userSeq));
                break;
            case "lecture_history":
                model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
                break;
            case "subscriptions":
            	//디버깅용
                System.out.println("[Controller] case=‘subscriptions’, userSeq=" + userSeq);
                List<SubscriptionDTO> subs = mpSV.getSubscriptions(userSeq);
                System.out.println("[Controller] getSubscriptions → size=" + subs.size() + ", data=" + subs);
                model.addAttribute("subscriptionList", subs);
                break;
//                model.addAttribute("subscriptionList", mpSV.getSubscriptions(userSeq));
//                break;
            case "dashboard":
            default:
                model.addAttribute("myData", mpSV.getMyPageData(userSeq));
                break;
        }

        model.addAttribute("currentTab", tab);
        return "mypage/mypage_main";
    }

    /** 대시보드 단독 접근용 (직접 경로 접근시) */
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
    	long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("myData", mpSV.getMyPageData(userSeq));
        return "mypage/dashboard";
    }

    /** 수강 내역 */
    @GetMapping("/lecture_history")
    public String lectureHistory(Authentication auth, Model model) {
    	long userSeq = getOrInitUserSeq(auth);
        model.addAttribute("lectureList", mpSV.getLectureHistory(userSeq));
        model.addAttribute("myLectureList", mpSV.selectMyLectures(userSeq));
        return "mypage/lecture_history";
    }

    @PostMapping("/unsubscribe")
    public boolean cancelSubscription(HttpSession session,
                                      @RequestParam Long instructorId) {
        Long userSeq = (Long) session.getAttribute("user_seq");
        return mpSV.cancelSubscription(userSeq, instructorId);
    }


    /** 내 정보 */
    @GetMapping("/my_info")
    public String myInfo(Authentication auth, Model model) {
    	long userSeq = getOrInitUserSeq(auth);
        System.out.println("uploadProfile 메서드 진입");
        model.addAttribute("myData", mpSV.getUserInfo(userSeq));
        return "mypage/my_info";
    }

    @Value("${file.upload-dir.profile}")
    private String uploadDir;

    @Value("${upload.path.profile}")
    private String uploadPath;

    /** 프로필 업로드 */
    @PostMapping("/upload_profile")
    @ResponseBody
    public Map<String, Object> uploadProfile(@RequestParam("file") MultipartFile file,
                                             Authentication auth) {
        Map<String, Object> result = new HashMap<>();

        Object raw = auth.getPrincipal();
        if (!(raw instanceof Long userSeq) || file.isEmpty()) {
            System.out.println("principal = " + auth.getPrincipal());
            result.put("success", false);
            result.put("message", "사용자 정보 또는 파일이 없습니다.");
            return result;
        }

        try {
            // 확장자 체크
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
            if (!List.of("png", "jpg", "jpeg", "gif").contains(ext)) {
                result.put("success", false);
                result.put("message", "이미지 파일만 업로드 가능합니다.");
                return result;
            }

            // 실제 파일 저장 경로
            String filename = "profile_" + userSeq + "." + ext;
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs(); // 폴더 없으면 생성
            }
            File dest = new File(uploadFolder, filename);
            file.transferTo(dest);

            // DB에 저장되는 경로는 "/userprofile/파일명" 형식으로
            String dbPath = "/userprofile/" + filename;
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

    /** 프로필 삭제 */
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
            // DB에서 경로 조회
            String profilePath = mpSV.selectProfilePath(userSeq);
            if (profilePath != null && !profilePath.isBlank()) {
                // 경로: "/userprofile/profile_410.png" → 실제 파일 경로 계산
                File file = new File(uploadDir, profilePath.replace("/userprofile/", ""));
                if (file.exists()) {
                    file.delete();
                }
            }

            // DB 경로 null 처리
            mpSV.updateUserProfile(userSeq, null);
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "삭제 중 오류 발생");
        }

        return result;
    }



    /** 비밀번호 변경 */
    @GetMapping("/reset_password")
    public String resetPassword() {
        return "mypage/reset_password";
    }

    /** 계좌 연동 */
    @GetMapping("/link_account")
    public String linkAccount() {
        return "mypage/link_account";
    }

    /** 탈퇴 페이지 */
    @GetMapping("/leave")
    public String leave() {
        return "mypage/leave";
    }
    
    @GetMapping("/subscriptions")
    public String subscriptionPage(Authentication auth, Model model) {
    	
    	//디버깅용//
    	long userSeq = getOrInitUserSeq(auth);
        System.out.println("[Controller·/subscriptions] called, userSeq=" + userSeq);
        List<SubscriptionDTO> subscriptions = mpSV.getSubscriptions(userSeq);
        System.out.println("[Controller·/subscriptions] from service → subscriptions("
                           + subscriptions.size() + ")=" + subscriptions);
        model.addAttribute("subscriptionList", subscriptions);
        return "mypage/subscriptions"; // subscriptions.html
    }

    @GetMapping("/wallet")
    public String accountPage(Model model,Authentication auth) {
    	long userSeq = getOrInitUserSeq(auth);
        FundingDTO accountInfo = fdSV.getAccountInfo(userSeq);
        model.addAttribute("accountInfo", accountInfo);
        return "mypage/wallet"; // 프래그먼트 지정
    }

    @GetMapping("/donation")
    public String fundingPage(Model model, Authentication auth) {
    	long userSeq = getOrInitUserSeq(auth);
        List<FundingDTO> fundingList = fdSV.getUserFundings(userSeq);
        model.addAttribute("fundingList", fundingList);
        model.addAttribute("donationType", "given"); // 추가된 라인
        return "mypage/donation";
    }

    /** 헤더/사이드바 (프래그먼트) */
    @GetMapping("/user_header")
    public String userHeader() {
        return "mypage/user_header";
    }

    @GetMapping("/sidebar")
    public String sidebarPage() {
        return "mypage/sidebar";
    }

}