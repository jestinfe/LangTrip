package kr.co.sist.e_learning.mypage;

import java.util.List;


public interface MyPageService {
    MyPageDTO getMyPageData(long userSeq);
    MyPageDTO getUserInfo(long userSeq);
    String selectProfilePath(long userSeq);
    void updateUserProfile(long userSeq, String newPath);
    List<LectureHistoryDTO> getLectureHistory(long userSeq);
    List<LectureHistoryDTO> selectMyLectures(long userSeq);
    UserAccountDTO getUserAccount(long userSeq);

    public boolean linkUserAccount(UserAccountDTO userAccountDTO);

    public boolean unlinkUserAccount(long userSeq);
    List<PaymentsDTO> getPaymentHistory(long userSeq);
    List<PaymentsDTO> getRefundablePayments(long userSeq);
    boolean requestRefund(long userSeq, RefundRequestDTO refundRequestDTO);
    List<RefundDTO> getRefundHistory(long userSeq);
    boolean requestSettlement(long userSeq);
    SettlementRequestDTO getPendingSettlementRequest(long userSeq);
    List<SettlementRequestDTO> getSettlementHistory(long userSeq);
    SettlementRequestDTO getSettlementDetail(long requestSeq);
}
    
    


