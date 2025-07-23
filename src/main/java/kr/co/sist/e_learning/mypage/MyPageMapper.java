package kr.co.sist.e_learning.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageMapper {
    MyPageDTO selectMyPageSummary(long userSeq);
    int selectTotalDonationByUserSeq(long userSeq);
    MyPageDTO selectUserInfo(long userSeq);
    String selectProfilePath(long userSeq);
    int updateProfile(Map<String, Object> paramMap);
    List<SubscriptionDTO> selectSubscriptions(long userSeq);
    int deleteSubscription(Map<String, Object> paramMap);
    UserAccountDTO selectUserAccount(long userSeq);
    int insertUserAccount(UserAccountDTO userAccountDTO);
    int updateUserAccount(UserAccountDTO userAccountDTO);
    int deleteUserAccount(long userSeq);
    List<LectureHistoryDTO> selectLectureHistory(long userSeq);
    List<LectureHistoryDTO> selectMyLectures(long userSeq);
    List<PaymentsDTO> selectPaymentHistory(long userSeq);
    List<PaymentsDTO> selectRefundablePayments(long userSeq);
    int updatePaymentStatus(Map<String, Object> paramMap);
    int insertRefund(RefundDTO refundDTO);
    PaymentsDTO selectPaymentByPaymentSeq(String paymentSeq);
    List<RefundDTO> selectRefundHistory(long userSeq);
    int insertSettlementRequest(SettlementRequestDTO settlementRequestDTO);
    SettlementRequestDTO selectPendingSettlementRequest(long userSeq);
    List<SettlementRequestDTO> selectSettlementHistory(long userSeq);
    SettlementRequestDTO selectSettlementDetail(long requestSeq);
}
