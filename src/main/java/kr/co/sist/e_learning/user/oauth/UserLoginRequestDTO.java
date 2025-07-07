package kr.co.sist.e_learning.user.oauth;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserLoginRequestDTO {
	private String email,password,socialProvider,socialId;
}
