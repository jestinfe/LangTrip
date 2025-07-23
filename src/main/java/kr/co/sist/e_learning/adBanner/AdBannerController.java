package kr.co.sist.e_learning.adBanner;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 광고 배너 관리자 컨트롤러
 */
@Controller
@RequestMapping("/admin/ad")
public class AdBannerController {

    private final AdBannerService service;

    public AdBannerController(AdBannerService service) {
        this.service = service;
    }

    /** 관리자 통계 페이지 */
    @GetMapping("/stats")
    public String statsPage(Model model) {
        // 상위 5개 배너와 6~10 배너
        List<AdBannerEntity> top5Stats = service.getTop5Banners();
        List<AdBannerEntity> next5Stats = service.getNext5Banners();
        model.addAttribute("top5Stats", top5Stats);
        model.addAttribute("next5Stats", next5Stats);
        return "admin/ad_stats";  // Thymeleaf 템플릿에서 표시할 페이지
    }

    /** 클릭 통계 기록 */
    @PostMapping("/click/{id}")
    @ResponseBody
    public ResponseEntity<Void> click(@PathVariable("id") Long id) {
        service.recordClick(id);  // 배너 클릭 기록
        return ResponseEntity.ok().build();  // 응답으로 성공 상태 반환
    }

    /** 사용자 화면에서 5개 배너 (1~5) 및 6~10 배너 JSON 으로 제공 */
    @GetMapping("/list")
    @ResponseBody
    public List<AdBannerEntity> list() {
        List<AdBannerEntity> top5Banners = service.getTop5Banners();
        List<AdBannerEntity> next5Banners = service.getNext5Banners();
        top5Banners.addAll(next5Banners); // 1~5와 6~10을 합침
        return top5Banners;  // 합쳐서 반환
    }
}
