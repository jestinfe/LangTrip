package kr.co.sist.e_learning.adBanner;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<AdBannerEntity> stats = service.getTop5Banners();
        model.addAttribute("stats", stats);
        return "admin/ad_stats";  // Thymeleaf 템플릿
    }

    /** 노출 통계 기록 */
    @PostMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Void> view(@PathVariable("id") Long id) {
        service.recordView(id);
        return ResponseEntity.ok().build();
    }

    /** 클릭 통계 기록 */
    @PostMapping("/click/{id}")
    @ResponseBody
    public ResponseEntity<Void> click(@PathVariable("id") Long id) {
        service.recordClick(id);
        return ResponseEntity.ok().build();
    }
    
    /** (선택) 사용자 화면에서 5개 배너 JSON 으로 제공 */
    @GetMapping("/list")
    @ResponseBody
    public List<AdBannerEntity> list() {
        return service.getTop5Banners();
    }
}
