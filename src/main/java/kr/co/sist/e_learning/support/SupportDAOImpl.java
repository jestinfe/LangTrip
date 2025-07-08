package kr.co.sist.e_learning.support;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupportDAOImpl implements SupportDAO {

	private static SupportDAOImpl sDAO;

	private SupportDAOImpl() {
	}// SupportDAOImpl

	public static SupportDAOImpl getInstance() {
		if (sDAO == null) {
			sDAO = new SupportDAOImpl();
		} // end if
		return sDAO;

	}// getInstance

	@Override
	public boolean insert(Object dto) {

		if (!(dto instanceof SupportDTOIdentifier)) {
			return false;
		}

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog")) {

			if (dto instanceof NoticeDTO notice) {

				// ID 생성
				String sql = "SELECT NVL(MAX(TO_NUMBER(notice_id)), 999) FROM notice WHERE REGEXP_LIKE(notice_id, '^[0-9]+$')";
				String newId = null;
				try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
					int id = 1000;
					if (rs.next()) {
						id = rs.getInt(1) + 1;
					}
					newId = String.valueOf(id);
					notice.setNotice_id(newId);
				}

				// INSERT
				try (PreparedStatement insertStmt = conn
						.prepareStatement("INSERT INTO notice (notice_id) VALUES (?)")) {
					insertStmt.setString(1, newId);
					int result = insertStmt.executeUpdate();
					System.out.println("[NoticeDTO] " + result + "건의 insert 시행.");
					return result > 0;
				}

			} else if (dto instanceof FaqDTO faq) {

				// ID 생성
				String sql = "SELECT NVL(MAX(TO_NUMBER(faq_id)), 1999) FROM faq WHERE REGEXP_LIKE(faq_id, '^[0-9]+$')";
				String newId = null;
				try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
					int id = 2000;
					if (rs.next()) {
						id = rs.getInt(1) + 1;
					}
					newId = String.valueOf(id);
					faq.setFaq_id(newId);
				}

				// INSERT
				try (PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO faq (faq_id) VALUES (?)")) {
					insertStmt.setString(1, newId);
					int result = insertStmt.executeUpdate();
					System.out.println("[FaqDTO] " + result + "건의 insert 시행.");
					return result > 0;
				}

			} else if (dto instanceof FeedbackDTO feedback) {

				// ID 생성
				String sql = "SELECT NVL(MAX(TO_NUMBER(feedback_id)), 2999) FROM feedback WHERE REGEXP_LIKE(feedback_id, '^[0-9]+$')";
				String newId = null;
				try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
					int id = 3000;
					if (rs.next()) {
						id = rs.getInt(1) + 1;
					}
					newId = String.valueOf(id);
					feedback.setFeedback_id(newId);
				}

				// INSERT
				try (PreparedStatement insertStmt = conn
						.prepareStatement("INSERT INTO feedback (feedback_id) VALUES (?)")) {
					insertStmt.setString(1, newId);
					int result = insertStmt.executeUpdate();
					System.out.println("[FeedbackDTO] " + result + "건의 insert 시행.");
					return result > 0;
				}

			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * @Override public List<Object> selectAll(String type) { List<Object>
	 * resultList = new ArrayList<>();
	 * 
	 * try (Connection conn =
	 * DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl",
	 * "c##under", "dog")) {
	 * 
	 * if (type.equals("notice")) { // Notice 조회 String noticeSql =
	 * "SELECT * FROM notice ORDER BY notice_created_date DESC"; try
	 * (PreparedStatement stmt = conn.prepareStatement(noticeSql); ResultSet rs =
	 * stmt.executeQuery()) { while (rs.next()) { NoticeDTO dto = new NoticeDTO();
	 * dto.setNotice_id(rs.getString("notice_id"));
	 * dto.setNotice_title(rs.getString("notice_title"));
	 * dto.setNotice_content(rs.getString("notice_content"));
	 * dto.setNotice_created_date(rs.getDate("notice_created_date"));
	 * dto.setNotice_fix_flag(rs.getString("notice_fix_flag"));
	 * dto.setNotice_writer(rs.getString("notice_writer"));
	 * dto.setNotice_hits(rs.getInt("notice_hits"));
	 * dto.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
	 * dto.setNotice_status(rs.getString("notice_status")); resultList.add(dto); }
	 * 
	 * System.out.println("[공지사항 목록]"); for (Object notice : resultList) {
	 * System.out.println(notice); } } } else if (type.equals("faq")) {
	 * 
	 * // Faq 조회 String faqSql = "SELECT * FROM faq ORDER BY faq_created_date DESC";
	 * try (PreparedStatement stmt = conn.prepareStatement(faqSql); ResultSet rs =
	 * stmt.executeQuery()) { while (rs.next()) { FaqDTO dto = new FaqDTO();
	 * dto.setFaq_id(rs.getString("faq_id"));
	 * dto.setFaqtype_id(rs.getString("faqtype_id"));
	 * dto.setFaq_title(rs.getString("faq_title"));
	 * dto.setFaq_content(rs.getString("faq_content"));
	 * dto.setFaq_created_date(rs.getDate("faq_created_date"));
	 * dto.setFaq_hits(rs.getInt("faq_hits"));
	 * dto.setFaq_lasted_modified_date(rs.getDate("faq_lasted_modified_date"));
	 * dto.setFaq_status(rs.getString("faq_status")); resultList.add(dto); }
	 * System.out.println("[FAQ 목록]"); for (Object faq : resultList) {
	 * System.out.println(faq); } } } else if (type.equals("feedback")) { //
	 * Feedback 조회 String feedbackSql =
	 * "SELECT * FROM feedback ORDER BY feedback_created_date DESC"; try
	 * (PreparedStatement stmt = conn.prepareStatement(feedbackSql); ResultSet rs =
	 * stmt.executeQuery()) { while (rs.next()) { FeedbackDTO dto = new
	 * FeedbackDTO(); dto.setFeedback_id(rs.getString("feedback_id"));
	 * dto.setUser_id(rs.getString("user_id"));
	 * dto.setCourse_seq(rs.getString("course_seq"));
	 * dto.setEmail(rs.getString("email"));
	 * dto.setFeedback_title(rs.getString("feedback_title"));
	 * dto.setFeedback_content(rs.getString("feedback_content"));
	 * dto.setFeedback_created_date(rs.getDate("feedback_created_date"));
	 * dto.setFeedback_status(rs.getString("feedback_status"));
	 * dto.setFeedback_step(rs.getString("feedback_step")); resultList.add(dto); }
	 * System.out.println("[피드백 목록]"); for (Object feedback : resultList) {
	 * System.out.println(feedback); } } } // end if
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); }
	 * 
	 * return resultList; }
	 */

	@Override
	public List<NoticeDTO> selectAllNoticeDTO(String type) {
		List<NoticeDTO> resultList = new ArrayList<>();

		String sql = "SELECT * FROM notice ORDER BY notice_created_date DESC";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog"); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setNotice_id(rs.getString("notice_id"));
				dto.setNotice_title(rs.getString("notice_title"));
				dto.setNotice_content(rs.getString("notice_content"));
				dto.setNotice_created_date(rs.getDate("notice_created_date"));
				dto.setNotice_fix_flag(rs.getString("notice_fix_flag"));
				dto.setNotice_writer(rs.getString("notice_writer"));
				dto.setNotice_hits(rs.getInt("notice_hits"));
				dto.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
				dto.setNotice_status(rs.getString("notice_status"));
				resultList.add(dto);
			}

			System.out.println("[공지사항 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public List<FaqDTO> selectAllFaqDTO(String type) {
		List<FaqDTO> resultList = new ArrayList<>();

		String sql = "SELECT * FROM faq ORDER BY faq_created_date DESC";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog"); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FaqDTO dto = new FaqDTO();
				dto.setFaq_id(rs.getString("faq_id"));
				dto.setFaqtype_id(rs.getString("faqtype_id"));
				dto.setFaq_title(rs.getString("faq_title"));
				dto.setFaq_content(rs.getString("faq_content"));
				dto.setFaq_created_date(rs.getDate("faq_created_date"));
				dto.setFaq_hits(rs.getInt("faq_hits"));
				dto.setFaq_lasted_modified_date(rs.getDate("faq_lasted_modified_date"));
				dto.setFaq_status(rs.getString("faq_status"));
				resultList.add(dto);
			}

			System.out.println("[FAQ 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public List<FeedbackDTO> selectAllFeedbackDTO(String type) {
		List<FeedbackDTO> resultList = new ArrayList<>();

		String sql = "SELECT * FROM feedback ORDER BY feedback_created_date DESC";

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog"); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				FeedbackDTO dto = new FeedbackDTO();
				dto.setFeedback_id(rs.getString("feedback_id"));
				dto.setUser_id(rs.getString("user_id"));
				dto.setCourse_seq(rs.getString("course_seq"));
				dto.setEmail(rs.getString("email"));
				dto.setFeedback_title(rs.getString("feedback_title"));
				dto.setFeedback_content(rs.getString("feedback_content"));
				dto.setFeedback_created_date(rs.getDate("feedback_created_date"));
				dto.setFeedback_status(rs.getString("feedback_status"));
				dto.setFeedback_step(rs.getString("feedback_step"));
				resultList.add(dto);
			}

			System.out.println("[피드백 목록]");
			resultList.forEach(System.out::println);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultList;
	}

	/*
	 * @Override public Object selectOneById(String type, String id) { Object dto =
	 * null;
	 * 
	 * try (Connection conn =
	 * DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl",
	 * "c##under", "dog")) {
	 * 
	 * if ("notice".equalsIgnoreCase(type)) { String sql =
	 * "SELECT * FROM notice WHERE notice_id = ?"; try (PreparedStatement stmt =
	 * conn.prepareStatement(sql)) { stmt.setString(1, id); try (ResultSet rs =
	 * stmt.executeQuery()) { if (rs.next()) { NoticeDTO notice = new NoticeDTO();
	 * notice.setNotice_id(rs.getString("notice_id"));
	 * notice.setNotice_title(rs.getString("notice_title"));
	 * notice.setNotice_content(rs.getString("notice_content"));
	 * notice.setNotice_created_date(rs.getDate("notice_created_date"));
	 * notice.setNotice_fix_flag(rs.getString("notice_fix_flag"));
	 * notice.setNotice_writer(rs.getString("notice_writer"));
	 * notice.setNotice_hits(rs.getInt("notice_hits"));
	 * notice.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
	 * notice.setNotice_status(rs.getString("notice_status")); dto = notice; } } }
	 * 
	 * } else if ("faq".equalsIgnoreCase(type)) { String sql =
	 * "SELECT * FROM faq WHERE faq_id = ?"; try (PreparedStatement stmt =
	 * conn.prepareStatement(sql)) { stmt.setString(1, id); try (ResultSet rs =
	 * stmt.executeQuery()) { if (rs.next()) { FaqDTO faq = new FaqDTO();
	 * faq.setFaq_id(rs.getString("faq_id"));
	 * faq.setFaqtype_id(rs.getString("faqtype_id"));
	 * faq.setFaq_title(rs.getString("faq_title"));
	 * faq.setFaq_content(rs.getString("faq_content"));
	 * faq.setFaq_created_date(rs.getDate("faq_created_date"));
	 * faq.setFaq_hits(rs.getInt("faq_hits"));
	 * faq.setFaq_lasted_modified_date(rs.getDate("faq_lasted_modified_date"));
	 * faq.setFaq_status(rs.getString("faq_status")); dto = faq; } } }
	 * 
	 * } else if ("feedback".equalsIgnoreCase(type)) { String sql =
	 * "SELECT * FROM feedback WHERE feedback_id = ?"; try (PreparedStatement stmt =
	 * conn.prepareStatement(sql)) { stmt.setString(1, id); try (ResultSet rs =
	 * stmt.executeQuery()) { if (rs.next()) { FeedbackDTO feedback = new
	 * FeedbackDTO(); feedback.setFeedback_id(rs.getString("feedback_id"));
	 * feedback.setUser_id(rs.getString("user_id"));
	 * feedback.setCourse_seq(rs.getString("course_seq"));
	 * feedback.setEmail(rs.getString("email"));
	 * feedback.setFeedback_title(rs.getString("feedback_title"));
	 * feedback.setFeedback_content(rs.getString("feedback_content"));
	 * feedback.setFeedback_created_date(rs.getDate("feedback_created_date"));
	 * feedback.setFeedback_status(rs.getString("feedback_status"));
	 * feedback.setFeedback_step(rs.getString("feedback_step")); dto = feedback; } }
	 * } }
	 * 
	 * } catch (SQLException e) { e.printStackTrace(); }
	 * 
	 * return dto; }
	 */

	@Override
	public NoticeDTO selectOneNoticeDTOById(String type, String id) {
		NoticeDTO notice = null;

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog")) {
			String sql = "SELECT * FROM notice WHERE notice_id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						notice = new NoticeDTO();
						notice.setNotice_id(rs.getString("notice_id"));
						notice.setNotice_title(rs.getString("notice_title"));
						notice.setNotice_content(rs.getString("notice_content"));
						notice.setNotice_created_date(rs.getDate("notice_created_date"));
						notice.setNotice_fix_flag(rs.getString("notice_fix_flag"));
						notice.setNotice_writer(rs.getString("notice_writer"));
						notice.setNotice_hits(rs.getInt("notice_hits"));
						notice.setNotice_last_modified_date(rs.getDate("notice_last_modified_date"));
						notice.setNotice_status(rs.getString("notice_status"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notice;
	}

	@Override
	public FaqDTO selectOneFaqDTOById(String type, String id) {
		FaqDTO faq = null;

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog")) {
			String sql = "SELECT * FROM faq WHERE faq_id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						faq = new FaqDTO();
						faq.setFaq_id(rs.getString("faq_id"));
						faq.setFaqtype_id(rs.getString("faqtype_id"));
						faq.setFaq_title(rs.getString("faq_title"));
						faq.setFaq_content(rs.getString("faq_content"));
						faq.setFaq_created_date(rs.getDate("faq_created_date"));
						faq.setFaq_hits(rs.getInt("faq_hits"));
						faq.setFaq_lasted_modified_date(rs.getDate("faq_lasted_modified_date"));
						faq.setFaq_status(rs.getString("faq_status"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return faq;
	}

	@Override
	public FeedbackDTO selectOneFeedbackDTOById(String type, String id) {
		FeedbackDTO feedback = null;

		try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.10.98:1521:orcl", "c##under",
				"dog")) {
			String sql = "SELECT * FROM feedback WHERE feedback_id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, id);
				try (ResultSet rs = stmt.executeQuery()) {
					if (rs.next()) {
						feedback = new FeedbackDTO();
						feedback.setFeedback_id(rs.getString("feedback_id"));
						feedback.setUser_id(rs.getString("user_id"));
						feedback.setCourse_seq(rs.getString("course_seq"));
						feedback.setEmail(rs.getString("email"));
						feedback.setFeedback_title(rs.getString("feedback_title"));
						feedback.setFeedback_content(rs.getString("feedback_content"));
						feedback.setFeedback_created_date(rs.getDate("feedback_created_date"));
						feedback.setFeedback_status(rs.getString("feedback_status"));
						feedback.setFeedback_step(rs.getString("feedback_step"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return feedback;
	}

	@Override
	public boolean update(Object dto) {
		// ID 식별 안되면 중단
		if (!(dto instanceof SupportDTOIdentifier)) {
			return false;
		}

		return false;
	}

	@Override
	public boolean delete(String id) {

		return false;
	}

}
