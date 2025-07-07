package kr.co.sist.e_learning.user.oauth;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SocialSignUpDTO {
	private String email,socialId,socialProvider,nickname,signupPath;;
}
