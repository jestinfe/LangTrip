package kr.co.sist.e_learning.admin.signup_req;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignupRequestDTO {
    private String requestId;
    public String adminId;
    private String adminName;
    private String email;
    private String phone;
    private String passwordHash;
    private String dept;
    private String status;
    private Timestamp requestDate;
    private List<String> roleCodes;
}