package kr.co.sist.e_learning.admin.auth;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminAuthDTO {
    private String adminId;
    private String adminPw;
    private String adminName;
    private String role;
    private String status;

    // getters & setters
}
