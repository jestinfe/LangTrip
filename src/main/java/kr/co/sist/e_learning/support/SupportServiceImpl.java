package kr.co.sist.e_learning.support;

import java.util.List;

public class SupportServiceImpl extends SupportServiceAbstract {

//	SupportDAO sDAO = SupportDAOImpl.getInstance();
	private final SupportDAO sDAO;

	public SupportServiceImpl(SupportDAO sDAO) {
		this.sDAO = sDAO;
	}

	@Override
	public boolean add(Object dto) {
		if (dto instanceof NoticeDTO noticeDto) {
			// Notice 추가 로직
			return sDAO.insert(noticeDto);
		} else if (dto instanceof FaqDTO faqDto) {
			// Faq 추가 로직
			return sDAO.insert(faqDto);
		} else if (dto instanceof FeedbackDTO feedbackDto) {
			// Faq 추가 로직
			return sDAO.insert(feedbackDto);
		} // end if
		return false;
	}// add

	@Override
	public List<NoticeDTO> searchAllNotice(String type) {
		return sDAO.selectAllNoticeDTO(type);
	}

	@Override
	public List<FaqDTO> searchAllFaq(String type) {
		return sDAO.selectAllFaqDTO(type);
	}

	@Override
	public List<FeedbackDTO> searchAllFeedback(String type) {
		return sDAO.selectAllFeedbackDTO(type);
	}

	@Override
	public NoticeDTO searchOneNotice(String type, String id) {
		// 특정 DTO 조회 로직
		return sDAO.selectOneNoticeDTOById(type, id);
	}// searchOne

	@Override
	public FaqDTO searchOneFaq(String type, String id) {
		// 특정 DTO 조회 로직
		return sDAO.selectOneFaqDTOById(type, id);
	}// searchOne

	@Override
	public FeedbackDTO searchOneFeedback(String type, String id) {
		// 특정 DTO 조회 로직
		return sDAO.selectOneFeedbackDTOById(type, id);
	}// searchOne

	@Override
	public boolean edit(String id) {
		return false;
	}

	@Override
	public boolean editDto(Object dto) {
		if (dto instanceof NoticeDTO noticeDto) {
			// Notice 수정 로직

			return true;
		} else if (dto instanceof FaqDTO faqDto) {
			// Faq 수정 로직

			return true;
		} else if (dto instanceof FeedbackDTO feedbackDto) {
			// Faq 수정 로직

			return true;
		}
		return false;
	}

	@Override
	public boolean remove(String id) {
		return false;
	}

	@Override
	public boolean removeDto(Object dto) {
		if (dto instanceof NoticeDTO noticeDto) {
			// Notice 삭제 로직

			return true;
		} else if (dto instanceof FaqDTO faqDto) {
			// Faq 삭제 로직

			return true;
		} else if (dto instanceof FeedbackDTO feedbackDto) {
			// Faq 삭제 로직

			return true;
		}
		return false;
	}

	@Override
	public boolean increaseHit(String id) {
		return false;
	}

	@Override
	public boolean increaseHitDto(Object dto) {
		if (dto instanceof NoticeDTO noticeDto) {
			// Notice 조회수 증가 로직

			return true;
		} else if (dto instanceof FaqDTO faqDto) {
			// Faq 조회수 증가 로직

			return true;
		} else if (dto instanceof FeedbackDTO feedbackDto) {
			// Faq 조회수 증가 로직

			return true;
		}
		return false;
	}

}
