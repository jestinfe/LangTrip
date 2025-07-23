package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.util.EncryptionUtil;


@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    @Qualifier("myPageDAOImpl")
    private MyPageDAO mpDAO;

    @Autowired
    private LectureHistoryDAO lctDAO;
    
    @Autowired
    private MyPageMapper myPageMapper;

    // 대시보드 요약 정보
    @Override
    public MyPageDTO getMyPageData(long userSeq) {
        return mpDAO.getUserInfo(userSeq);
    }

    @Override
    public MyPageDTO getUserInfo(long userSeq) {
        return mpDAO.getUserInfo(userSeq);
    }

    @Override
    public String selectProfilePath(long userSeq) {
        return mpDAO.selectProfilePath(userSeq);
    }
    
    //프로필 이미지 업로드
    @Override
    public void updateUserProfile(long userSeq, String newPath) {
       mpDAO.updateProfile(userSeq, newPath);
    }
    
    // 수강 내역
    @Override
    public List<LectureHistoryDTO> getLectureHistory(long userSeq) {
        return lctDAO.getLectureHistory(userSeq);
    }
    
    //내 강의
    @Override
    public List<LectureHistoryDTO> selectMyLectures(long userSeq) {
        return lctDAO.selectMyLectures(userSeq);
    }
       
    
    // 구독 목록
    @Override
    public List<SubscriptionDTO> getSubscriptions(Long userSeq) {
        System.out.println("[Service] getSubscriptions(userSeq=" + userSeq + ")");
        List<SubscriptionDTO> list = mpDAO.selectSubscriptions(userSeq);
        System.out.println("[Service] selectSubscriptions → size=" + list.size() + ", list=" + list);
        return list;
    }

    // 구독 취소
    @Override
    public boolean cancelSubscription(Long userSeq, Long instructorId) {
        System.out.println("[Service] cancelSubscription(userSeq=" + userSeq + ", instructorId=" + instructorId + ")");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userSeq", userSeq);
        paramMap.put("instructorId", instructorId);
        int deleted = mpDAO.deleteSubscription(paramMap);
        boolean result = deleted > 0;
        System.out.println("[Service] deleteSubscription → deleted=" + deleted + ", result=" + result);
        return result;
    }
    
    @Override
    public UserAccountDTO getUserAccount(long userSeq) {
        UserAccountDTO account = myPageMapper.selectUserAccount(userSeq);
        if (account != null) {
            account.setAccountNum(EncryptionUtil.decrypt(account.getAccountNum()));
            account.setHolderName(EncryptionUtil.decrypt(account.getHolderName()));
        }
        return account;
    }
    
    @Override
    public boolean linkUserAccount(UserAccountDTO userAccountDTO) {
        // 암호화
        userAccountDTO.setAccountNum(EncryptionUtil.encrypt(userAccountDTO.getAccountNum()));
        userAccountDTO.setHolderName(EncryptionUtil.encrypt(userAccountDTO.getHolderName()));

        // 기존 계좌 정보가 있는지 확인
        UserAccountDTO existingAccount = myPageMapper.selectUserAccount(userAccountDTO.getUserSeq());

        if (existingAccount != null) {
            // 기존 계좌가 있으면 업데이트
            return myPageMapper.updateUserAccount(userAccountDTO) > 0;
        } else {
            // 없으면 삽입
            return myPageMapper.insertUserAccount(userAccountDTO) > 0;
        }
    }

    @Override
    public boolean unlinkUserAccount(long userSeq) {
        return myPageMapper.deleteUserAccount(userSeq) > 0;
    }

    @Override
    public List<PaymentsDTO> getPaymentHistory(long userSeq) {
        return myPageMapper.selectPaymentHistory(userSeq);
    }

    

    @Override
    public boolean requestRefund(long userSeq, RefundRequestDTO refundRequestDTO) {
        // 1. 결제 정보 조회
        PaymentsDTO payment = myPageMapper.selectPaymentByPaymentSeq(refundRequestDTO.getPaymentId());
        if (payment == null || !payment.getUserSeq().equals(userSeq)) {
            // 결제 정보가 없거나, 해당 사용자의 결제가 아님
            return false;
        }

        // 2. 환불 가능 여부 확인 (이미 구현된 selectRefundablePayments 로직과 유사)
        // 여기서는 간단히 상태만 확인. 실제로는 후원 이력도 다시 확인해야 함.
        if (!"SUCCESS".equals(payment.getPaymentStatus())) {
            return false; // 이미 환불되었거나 다른 상태
        }

        // 3. 결제 상태 업데이트
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paymentSeq", refundRequestDTO.getPaymentId());
        paramMap.put("status", "REFUND_REQUESTED");
        myPageMapper.updatePaymentStatus(paramMap);

        // 4. 환불 기록 삽입
        UserAccountDTO userAccount = myPageMapper.selectUserAccount(userSeq);
        if (userAccount == null) {
            // 계좌 정보 없음 (사전 조건에서 걸러지지만, 혹시 모를 경우)
            return false;
        }

        RefundDTO refundDTO = new RefundDTO();
        refundDTO.setPaymentSeq(refundRequestDTO.getPaymentId());
        refundDTO.setAccountSeq(userAccount.getAccountSeq());
        refundDTO.setWalletSeq(payment.getWalletSeq()); // 결제 시 사용된 지갑 시퀀스
        refundDTO.setRefundAmount(payment.getPaymentAmount());
        refundDTO.setStatus("REQUESTED");
        refundDTO.setReason(refundRequestDTO.getRefundReason());

        return myPageMapper.insertRefund(refundDTO) > 0;
    }

    @Override
    public List<RefundDTO> getRefundHistory(long userSeq) {
        return myPageMapper.selectRefundHistory(userSeq);
    }



    @Override
    public List<PaymentsDTO> getRefundablePayments(long userSeq) {
        return myPageMapper.selectRefundablePayments(userSeq);
    }

   
}

