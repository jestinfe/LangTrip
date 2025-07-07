package kr.co.sist.e_learning.user.oauth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
	 private String token,userSeq,email,nickname,socialProvider;
}
