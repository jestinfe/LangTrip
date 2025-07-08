package kr.co.sist.e_learning.support;

import java.sql.Date;

public class FeedbackDTO implements SupportDTOIdentifier {
	private String feedback_id;
	private String user_id;
	private String course_seq;
	private String email;
	private String feedback_title;
	private String feedback_content;
	private Date feedback_created_date;
	private String feedback_status;
	private String feedback_step;

	@Override
	public String getId() {
		return this.getFeedback_id();
	}

	public String getFeedback_id() {
		return feedback_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getCourse_seq() {
		return course_seq;
	}

	public String getEmail() {
		return email;
	}

	public String getFeedback_title() {
		return feedback_title;
	}

	public String getFeedback_content() {
		return feedback_content;
	}

	public Date getFeedback_created_date() {
		return feedback_created_date;
	}

	public String getFeedback_status() {
		return feedback_status;
	}

	public String getFeedback_step() {
		return feedback_step;
	}

	public void setFeedback_id(String feedback_id) {
		this.feedback_id = feedback_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setCourse_seq(String course_seq) {
		this.course_seq = course_seq;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFeedback_title(String feedback_title) {
		this.feedback_title = feedback_title;
	}

	public void setFeedback_content(String feedback_content) {
		this.feedback_content = feedback_content;
	}

	public void setFeedback_created_date(Date feedback_created_date) {
		this.feedback_created_date = feedback_created_date;
	}

	public void setFeedback_status(String feedback_status) {
		this.feedback_status = feedback_status;
	}

	public void setFeedback_step(String feedback_step) {
		this.feedback_step = feedback_step;
	}

	@Override
	public String toString() {
		return "FeedbackDTO [feedback_id=" + feedback_id + ", user_id=" + user_id + ", course_seq=" + course_seq
				+ ", email=" + email + ", feedback_title=" + feedback_title + ", feedback_content=" + feedback_content
				+ ", feedback_created_date=" + feedback_created_date + ", feedback_status=" + feedback_status
				+ ", feedback_step=" + feedback_step + "]";
	}

}// class
