package kr.co.sist.e_learning.user.oauth;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LocalSignUpDTO {
	private String email,password,nickname,signupPath;
}
