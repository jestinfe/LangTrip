package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImpl implements MyPageService {

	 @Autowired
	 @Qualifier("myPageDAOImpl")
	 private MyPageDAO mpDAO;

    @Autowired
    private MyPageMapper myPageMapper;
    
    @Autowired
    private LectureHistoryDAO lctDAO;

    @Autowired
    private FundingService fundingService; // FundingService 주입
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 대시보드 요약 정보
    @Override
    public MyPageDTO getMyPageData(long userSeq) {
        return myPageMapper.selectMyPageSummary(userSeq);
    }

    @Override
    public MyPageDTO getUserInfo(long userSeq) {
        return mpDAO.getUserInfo(userSeq);

    }

    @Override
    public String selectProfilePath(long userSeq) {
        String profilePath = mpDAO.selectProfilePath(userSeq);
        if (profilePath == null || profilePath.isEmpty()) {
            throw new RuntimeException("프로필 경로가 존재하지 않습니다.");
        }
        return profilePath;
    }
    
    @Override
    public void updateUserProfile(long userSeq, String newPath) {
        if (newPath == null || newPath.isEmpty()) {
            throw new IllegalArgumentException("프로필 경로가 유효하지 않습니다.");
        }
        mpDAO.updateProfile(userSeq, newPath);
    }
    
    //회원탈퇴
    @Override
    public boolean checkPassword(long userSeq, String inputPassword) {
        String hashed = mpDAO.getUserPassword(userSeq);
        return passwordEncoder.matches(inputPassword, hashed);
    }

    @Override
    public boolean withdrawUser(long userSeq, int reasonCode) {
        return mpDAO.updateWithdrawalStatus(userSeq, reasonCode) > 0;
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
    public List<PaymentsDTO> getPaymentHistory(long userSeq) {
        return myPageMapper.selectPaymentHistory(userSeq);
    }

    @Override
    public List<PaymentsDTO> getRefundablePayments(long userSeq) {
        return myPageMapper.selectRefundablePayments(userSeq);
    }
    
    @Override
    public String getUserNickname(long userSeq) {
        return mpDAO.getUserNickname(userSeq);
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
        if (!"payment_success".equals(payment.getPaymentStatus())) {
            return false; // 이미 환불되었거나 다른 상태
        }

        // 3. 결제 상태 업데이트
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paymentSeq", refundRequestDTO.getPaymentId());
        paramMap.put("status", "refund_requested");
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
        refundDTO.setStatus("refund_requested");
        refundDTO.setReason(refundRequestDTO.getRefundReason());

        return myPageMapper.insertRefund(refundDTO) > 0;
    }

    @Override
    public List<RefundDTO> getRefundHistory(long userSeq) {
        return myPageMapper.selectRefundHistory(userSeq);
    }

    @Override
    public boolean requestSettlement(long userSeq) {
        // 1. 계좌 등록 여부 확인
        UserAccountDTO userAccount = myPageMapper.selectUserAccount(userSeq);
        if (userAccount == null) {
            return false; // 계좌 미등록
        }

        // 2. 진행 중인 정산이 있는지 확인
        SettlementRequestDTO pendingRequest = myPageMapper.selectPendingSettlementRequest(userSeq);
        if (pendingRequest != null) {
            return false; // 진행 중인 정산이 있음
        }

        // 3. 정산 가능 마일리지 조회
        FundingDTO fundingInfo = fundingService.getAccountInfo(userSeq);
        double availableMiles = fundingInfo != null ? fundingInfo.getReceivedMiles() : 0;

        if (availableMiles <= 0) {
            return false; // 정산할 마일리지가 없음
        }

        // 4. 정산 신청 기록 생성
        double companyFee = 3.3; // 수수료 3.3%
        double paidAmount = availableMiles * (100 - companyFee) / 100;

        SettlementRequestDTO settlementRequest = new SettlementRequestDTO();
        settlementRequest.setUserSeq(userSeq);
        settlementRequest.setWalletSeq(fundingInfo.getWalletSeq());
        settlementRequest.setTotalMile(availableMiles);
        settlementRequest.setCompanyFee(companyFee);
        settlementRequest.setPaidAmount(paidAmount);
        settlementRequest.setStatus("settlement_requested");
        settlementRequest.setAccountSeq(userAccount.getAccountSeq());

        return myPageMapper.insertSettlementRequest(settlementRequest) > 0;
    }


    @Override
    public SettlementRequestDTO getPendingSettlementRequest(long userSeq) {
        return myPageMapper.selectPendingSettlementRequest(userSeq);
    }

    @Override
    public List<SettlementRequestDTO> getSettlementHistory(long userSeq) {
        return myPageMapper.selectSettlementHistory(userSeq);
    }

    @Override
    public SettlementRequestDTO getSettlementDetail(long requestSeq) {
        return myPageMapper.selectSettlementDetail(requestSeq);
    }
    
    @Override
    public boolean unlinkUserAccount(long userSeq) {
        int refundCount = myPageMapper.countRefundByUserSeq(userSeq);
        int payoutCount = myPageMapper.countSettlementByUserSeq(userSeq);

        if (refundCount > 0 || payoutCount > 0) {
            throw new IllegalStateException("정산 또는 환불 내역이 존재하여 계좌 연동을 해제할 수 없습니다.");
        }

        return myPageMapper.deleteUserAccount(userSeq) > 0;
    }
}

