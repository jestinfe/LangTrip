package kr.co.sist.e_learning.mypage;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageServiceImpl implements MyPageService {

    @Autowired
    private MyPageDAO dao;

    @Autowired
    private LectureHistoryDAO lectureHistoryDAO;

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    // 대시보드 요약 정보
    @Override
    public MyPageDTO getMyPageData(String userSeq) {
        return dao.getMyPageSummary(userSeq);
    }

    // 내 정보 조회
    @Override
    public MyPageDTO getUserInfo(String userSeq) {
        return dao.selectUserInfo(userSeq); // ⬅ static 호출이 아니라 인스턴스 메서드 호출로 수정
    }

    // 수강 내역
    @Override
    public List<LectureHistoryDTO> getLectureHistory(String userSeq) {
        return lectureHistoryDAO.getLectureHistory(userSeq);
    }

    // 구독 목록
    @Override
    public List<SubscriptionDTO> getSubscriptions(String userSeq) {
        return subscriptionDAO.selectSubscriptions(userSeq);
    }

    // 구독 취소
    @Override
    public int cancelSubscription(String followerId, String followeeId) {
        return subscriptionDAO.deleteSubscription(followerId, followeeId);
    }
}
