package kr.co.sist.e_learning.payment;

import kr.co.sist.e_learning.admin.payments.AdminMileageStatsDTO;
import kr.co.sist.e_learning.admin.payments.AdminPaymentListDTO;
import kr.co.sist.e_learning.admin.payments.AdminPaymentSearchDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentRequestDTO dto);
    List<AdminPaymentListDTO> selectAdminPaymentList(AdminPaymentSearchDTO searchDTO);
    int selectAdminPaymentCount(AdminPaymentSearchDTO searchDTO);
    AdminMileageStatsDTO selectAdminMileageStats();
}
