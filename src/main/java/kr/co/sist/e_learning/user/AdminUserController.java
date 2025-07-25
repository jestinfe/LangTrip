package kr.co.sist.e_learning.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.sist.e_learning.pagination.UsrAndRptPageRequestDTO;
import kr.co.sist.e_learning.pagination.UsrAndRptPageResponseDTO;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {
	private final AdminUserService adminUserService;
	
	@Autowired
	public AdminUserController(AdminUserService adminUserService) {
		this.adminUserService = adminUserService;
	}
	
	@GetMapping("/user-list")
	public String userList(UsrAndRptPageRequestDTO requestDTO, Model model) {
		UsrAndRptPageResponseDTO<UserDTO> responseDTO = adminUserService.getUserList(requestDTO);
		model.addAttribute("requestDTO", requestDTO);
		model.addAttribute("responseDTO", responseDTO);
		
		System.out.println("✅ /admin/user/user-list 접근됨");
		
		return "admin/user/user-list";
	}
	
	@GetMapping("/detail")
	@ResponseBody
	public UserDTO getUserDetail(@RequestParam("userSeq") Long userSeq) {
		return adminUserService.getUserDetail(userSeq);
	}
	
	@PostMapping("/update-status")
	@ResponseBody
	public ResponseEntity<String> updateUserStatus(@RequestBody UserDTO dto) {
		try {
			adminUserService.updateUserStatus(dto);
			return ResponseEntity.ok("성공");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("실패");
		}
	}
}
