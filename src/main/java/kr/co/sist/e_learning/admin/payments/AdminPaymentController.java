package kr.co.sist.e_learning.admin.payments;

import kr.co.sist.e_learning.payment.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminPaymentController {

    private final PaymentMapper paymentMapper;

    @Autowired
    public AdminPaymentController(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @GetMapping("/admin/payments")
    @PreAuthorize("hasAnyRole('PAYMENT', 'SUPER')")
    public String adminPayments(
            @RequestParam(value = "viewType", defaultValue = "payment") String viewType,
            @RequestParam(value = "paymentType", defaultValue = "all") String paymentType,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "async", defaultValue = "false") boolean async,
            Model model) {

        AdminPaymentSearchDTO searchDTO = new AdminPaymentSearchDTO();
        searchDTO.setViewType(viewType);
        searchDTO.setPaymentType(paymentType);
        searchDTO.setSearchKeyword(searchKeyword);
        searchDTO.setStartDate(startDate);
        searchDTO.setEndDate(endDate);
        searchDTO.setPageSize(pageSize);
        searchDTO.setPage(page);
        searchDTO.setStartRow((page - 1) * pageSize);

        List<AdminPaymentListDTO> paymentList = null;
        AdminMileageStatsDTO mileageStats = null;
        int totalCount = 0;

        if ("payment".equals(viewType)) {
            paymentList = paymentMapper.selectAdminPaymentList(searchDTO);
            totalCount = paymentMapper.selectAdminPaymentCount(searchDTO);
        } else if ("mileage".equals(viewType)) {
            mileageStats = paymentMapper.selectAdminMileageStats();
        }

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        int startPage = Math.max(1, page - 2);
        int endPage = Math.min(totalPages, page + 2);

        model.addAttribute("viewType", viewType);
        model.addAttribute("paymentType", paymentType);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("page", page);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("paymentList", paymentList);
        model.addAttribute("mileageStats", mileageStats);

        if (async) {
            if ("payment".equals(viewType)) {
                return "admin/payments/payment_list_fragment";
            } else {
                return "admin/payments/mileage_wallet_fragment";
            }
        } else {
            return "admin/payments/admin_payment";
        }
    }
}
