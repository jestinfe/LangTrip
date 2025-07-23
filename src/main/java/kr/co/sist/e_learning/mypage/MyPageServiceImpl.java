package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.sist.e_learning.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    private MyPageMapper myPageMapper;

    // 대시보드 요약 정보
    @Override
    public MyPageDTO getMyPageData(long userSeq) {
        return myPageMapper.selectMyPageSummary(userSeq);
    }

    @Override
    public MyPageDTO getUserInfo(long userSeq) {
        return myPageMapper.selectUserInfo(userSeq);
    }

    @Override
    public String selectProfilePath(long userSeq) {
        return myPageMapper.selectProfilePath(userSeq);
    }
    
    //프로필 이미지 업로드
    @Override
    public void updateUserProfile(long userSeq, String newPath) {
    	Map<String, Object> paramMap = new HashMap<>();
    	paramMap.put("userSeq", userSeq);
    	paramMap.put("profile", newPath);
    	myPageMapper.updateProfile(paramMap);
    }
    
    // 수강 내역
    @Override
    public List<LectureHistoryDTO> getLectureHistory(long userSeq) {
        return myPageMapper.selectLectureHistory(userSeq);
    }
    
    //내 강의
    @Override
    public List<LectureHistoryDTO> selectMyLectures(long userSeq) {
        return myPageMapper.selectMyLectures(userSeq);
    }
    	
    
    // 구독 목록
    @Override
    public List<SubscriptionDTO> getSubscriptions(Long userSeq) {
        System.out.println("[Service] getSubscriptions(userSeq=" + userSeq + ")");
        List<SubscriptionDTO> list = myPageMapper.selectSubscriptions(userSeq);
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
        int deleted = myPageMapper.deleteSubscription(paramMap);
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

}
