package kr.co.sist.e_learning.user.oauth;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailVerificationDTO {
	private String verificationSeq, email, code, status;
	private LocalDateTime createdAt, expiresAt;
}
