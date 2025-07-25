package kr.co.sist.e_learning.admin.donation;

import kr.co.sist.e_learning.admin.PageRequestDTO_donation;
import kr.co.sist.e_learning.admin.PageResponseDTO_donation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
public class AdminDonationController {

    private final AdminDonationService adminDonationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DONATION', 'SUPER')")
    public String donationList(Model model,
                               DonationSearchDTO searchDTO,
                               @ModelAttribute PageRequestDTO_donation pageRequestDTO,
                               @RequestParam(value = "async", required = false, defaultValue = "false") boolean async) {

        PageResponseDTO_donation<DonationVO> responseDTO = adminDonationService.getDonationList(searchDTO, pageRequestDTO);
        model.addAttribute("pageResponse", responseDTO);
        model.addAttribute("searchDTO", searchDTO);

        if (async) {
            return "admin/donation/donation_list_fragment :: donationList"; // Thymeleaf fragment
        }

        return "admin/donation/admin_donation";
    }

    @PostMapping("/deleteMessage/{donationId}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('DONATION', 'SUPER')")
    public ResponseEntity<String> deleteMessage(@PathVariable String donationId) {
       

        boolean success = adminDonationService.deleteDonationMessage(donationId);
        if (success) {
          
            return ResponseEntity.ok("Message deleted successfully");
        } else {
            
            return ResponseEntity.badRequest().body("Failed to delete message");
        }
    }
}