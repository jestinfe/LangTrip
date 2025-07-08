package kr.co.sist.e_learning.support;

import java.util.List;

public interface SupportService {

	public boolean add(Object dto);// 항목추가

	public List<NoticeDTO> searchAllNotice(String type);// 전체 NoticeDTO 조회

	public List<FaqDTO> searchAllFaq(String type);// 전체 FaqDTO 조회

	public List<FeedbackDTO> searchAllFeedback(String type);// 전체 FeedbackDTO 조회

	public NoticeDTO searchOneNotice(String type, String id);// 상세 NoticeDTO 조회

	public FaqDTO searchOneFaq(String type, String id);// 상세 FaqDTO 조회

	public FeedbackDTO searchOneFeedback(String type, String id);// 상세 FeedbackDTO 조회

	public boolean edit(String id);// 내용수정

	public boolean editDto(Object dto);

	public boolean remove(String id);// 항목삭제

	public boolean removeDto(Object dto);

	public boolean increaseHit(String id);// 조회수 증가

	public boolean increaseHitDto(Object dto);

	//////////////////////////////////////////////////////////////

	public String identifyDtoToCurrentId(Object dto);

//	public String safeIdentifyDtoId(Object dto);

}
