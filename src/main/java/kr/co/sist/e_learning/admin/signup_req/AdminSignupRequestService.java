package kr.co.sist.e_learning.admin.signup_req;

import java.util.List;

public interface AdminSignupRequestService {
    List<AdminSignupRequestDTO> listPendingRequests();
    AdminSignupRequestDTO getRequestById(String requestId);
    void approveRequest(String requestId);
    void rejectRequest(String requestId);
}