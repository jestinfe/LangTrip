package kr.co.sist.e_learning.mypage;

import java.util.List;

public interface LectureHistoryDAO {
    List<LectureHistoryDTO> getLectureHistory(String userSeq);
}
