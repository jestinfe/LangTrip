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

    @Autowired
    private SubscriptionDAO sbDAO;

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
    
    //구독목록
    @Override
    public List<SubscriptionDTO> getSubscriptions(long userSeq) {
        return sbDAO.selectSubscriptions(userSeq);
    }

    //구독취소
    @Override
    public int cancelSubscription(long followerId, String followeeId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userSeq", followerId);
        map.put("instructorId", followeeId);
        sbDAO.deleteSubscription(map);
        return 1; // 성공 시 1 반환. 실제 영향 줄 수는 DAO 쪽에서 반환값 받아도 됨
    }

}
