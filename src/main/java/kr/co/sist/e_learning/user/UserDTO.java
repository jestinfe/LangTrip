package kr.co.sist.e_learning.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userSeq;
    private String email;
    private String password;
    private String nickname;
    private String status;
    private String profile;
    private String socialId;
    private String socialProvider;
}
