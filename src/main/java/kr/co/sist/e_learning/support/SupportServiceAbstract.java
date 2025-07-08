package kr.co.sist.e_learning.support;

import java.util.List;

public abstract class SupportServiceAbstract implements SupportService {

	public abstract boolean add(Object dto);// 항목추가

	public abstract List<NoticeDTO> searchAllNotice(String type);// 전체조회

	public abstract List<FaqDTO> searchAllFaq(String type);// 전체조회

	public abstract List<FeedbackDTO> searchAllFeedback(String type);// 전체조회

	public abstract NoticeDTO searchOneNotice(String type, String id);// 상세 NoticeDTO 조회

	public abstract FaqDTO searchOneFaq(String type, String id);// 상세 FaqDTO 조회

	public abstract FeedbackDTO searchOneFeedback(String type, String id);// 상세 FeedbackDTO 조회

	public abstract boolean edit(String id);// 내용수정

	public abstract boolean editDto(Object dto);// 내용수정(실제 DAO활용)

	public abstract boolean remove(String id);// 항목삭제

	public abstract boolean removeDto(Object dto);// 항목삭제(실제 DAO활용)

	public abstract boolean increaseHit(String id);// 조회수 증가

	public abstract boolean increaseHitDto(Object dto);// 조회수 증가(실제 DAO활용)

	//////////////////////////////////////////////////////////////

	@Override
	public String identifyDtoToCurrentId(Object dto) {
		if (dto instanceof SupportDTOIdentifier identifier) {
			return identifier.getId();
		}
		throw new IllegalArgumentException("지원되지 않는 DTO 타입입니다: " + dto.getClass().getSimpleName());
	}

//	@Override
//	public String safeIdentifyDtoId(Object dto) {
//		try {
//			return identifyDtoToCurrentId(dto);
//		} catch (IllegalArgumentException e) {
//			System.err.println("ID 추출 실패: " + e.getMessage());
//			return null;
//		}
//	}
}
