package kr.co.sist.e_learning.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    @Qualifier("myPageDAOImpl")
    private MyPageDAO mpDAO;

    @Autowired
    private LectureHistoryDAO lctDAO;

    // 대시보드 요약 정보
    @Override
    public MyPageDTO getMyPageData(long userSeq) {
        return mpDAO.getMyPageSummary(userSeq);
    }

    // 내 정보 조회
    @Override
    public MyPageDTO getUserInfo(long userSeq) {
        return mpDAO.selectUserInfo(userSeq); // ⬅ static 호출이 아니라 인스턴스 메서드 호출로 수정
    }

    @Override
    public String selectProfilePath(long userSeq) {
        return mpDAO.selectProfilePath(userSeq);
    }
    
    //프로필 이미지 업로드
    @Override
    public void updateProfilePath(long userSeq, String newPath) {
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

}
