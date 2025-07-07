package kr.co.sist.e_learning.user.oauth;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private String userSeq,email,password,nickname,signupPath,
     unsignPath,status,socialId,socialProvider,encryptedAccountNum,encryptedHolderName;
    private LocalDate createdAt;
}
