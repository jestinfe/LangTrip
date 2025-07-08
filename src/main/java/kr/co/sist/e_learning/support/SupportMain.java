package kr.co.sist.e_learning.support;

public class SupportMain {

	public static void main(String[] args) {
//		SupportDAO dao = new SupportDAOImpl(); // DAO 구현체를 생성
		SupportDAO dao = SupportDAOImpl.getInstance();
		SupportService ssi = new SupportServiceImpl(dao); // DAO 주입
		NoticeDTO nDTO = new NoticeDTO();
		FaqDTO faqDTO = new FaqDTO();
		FeedbackDTO feedDTO = new FeedbackDTO();
		ssi.add(nDTO);
		ssi.add(nDTO);
		ssi.add(nDTO);
		ssi.add(faqDTO);
		ssi.add(feedDTO);
		ssi.add(nDTO);
		ssi.add(faqDTO);
		ssi.add(feedDTO);
		ssi.add(faqDTO);
		ssi.add(feedDTO);

//		ssi.searchAll("notice");
//		ssi.searchAll("faq");
//		ssi.searchAll("feedback");

		System.out.println("[특정 공지사항 조회(1000~1999)] -> " + ssi.searchOneNotice("notice", "1001"));
		System.out.println("[특정 FAQ 조회(2000~2999)] -> " + ssi.searchOneFaq("faq", "2001"));
		System.out.println("[특정 피드백 조회(3000~3999)] -> " + ssi.searchOneFeedback("feedback", "3001"));

	}

}
