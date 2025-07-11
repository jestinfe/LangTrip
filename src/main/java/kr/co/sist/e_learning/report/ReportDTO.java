package kr.co.sist.e_learning.report;

import java.sql.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ReportDTO {
	private int reportId;
	private Date reportedAt;
	private String contentType;
	private String title;
	private String reporterId;
	private String reportedUserId;
	private String nickName;
	
	private List<String> reporterCheckedReason;
	private List<String> adminCheckedReason;
	private String reporterCustomReasonTxt;
	private String adminCustomReasonTxt;
	
	private String actionStatus;
	private String reportedUserStatus;
	private String adminNewStatus;
	
	private int postId2;
	private int courseId;
}
