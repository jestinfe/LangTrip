package kr.co.sist.e_learning.support;

import java.util.List;

public interface SupportDAO {

	boolean insert(Object dto); // 항목 추가

	List<NoticeDTO> selectAllNoticeDTO(String type); // 전체 항목 조회

	List<FaqDTO> selectAllFaqDTO(String type); // 전체 항목 조회

	List<FeedbackDTO> selectAllFeedbackDTO(String type); // 전체 항목 조회

	NoticeDTO selectOneNoticeDTOById(String type, String id); // ID로 단일 항목 조회

	FaqDTO selectOneFaqDTOById(String type, String id); // ID로 단일 항목 조회

	FeedbackDTO selectOneFeedbackDTOById(String type, String id); // ID로 단일 항목 조회

	boolean update(Object dto); // 항목 수정

	boolean delete(String id); // ID로 항목 삭제
}
