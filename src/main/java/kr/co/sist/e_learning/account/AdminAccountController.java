//package kr.co.sist.e_learning.admin.account;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/account")
//public class AdminAccountController {
//
//    @Autowired
//    private AdminAccountService accountService;
//
//    @GetMapping("/account_list")
//    public String listAdmins(Model model) {
//        List<AdminAccountDTO> adminList = accountService.getAllAdmins();
//        model.addAttribute("adminList", adminList);
//        return "admin/account_list";
//    }
//
//    @GetMapping("/account_detail")
//    public String adminDetail(@RequestParam String adminId, Model model) {
//        AdminAccountDTO admin = accountService.getAdminDetail(adminId);
//        model.addAttribute("admin", admin);
//        return "admin/account_detail";
//    }
//
//    @PostMapping("/update")
//    public String updateAdmin(AdminAccountDTO dto) {
//        accountService.updateAdmin(dto);
//        return "redirect:/admin/account/detail?adminId=" + dto.getAdminId();
//    }
//}
