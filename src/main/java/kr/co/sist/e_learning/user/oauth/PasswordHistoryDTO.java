package kr.co.sist.e_learning.user.oauth;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordHistoryDTO {
	private String userSeq, passwordHash;
	private LocalDateTime createdAt;
}
