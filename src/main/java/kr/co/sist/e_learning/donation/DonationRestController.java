package kr.co.sist.e_learning.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import kr.co.sist.e_learning.user.auth.SimpleResponseDTO;

@RestController
@RequestMapping("/api/donation")
public class DonationRestController {

    @Autowired
    private DonationService donationService;

    @PostMapping
    public ResponseEntity<?> donate(@RequestBody DonationRequestDTO dto, Authentication authentication) {
        try {
            Long userSeq = (Long) authentication.getPrincipal();
            DonationDTO donationResult = donationService.donate(userSeq, dto);
            return ResponseEntity.ok(donationResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SimpleResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new SimpleResponseDTO(false, "후원 처리 중 오류가 발생했습니다.", null));
        }
    }
}

