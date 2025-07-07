// 2. DAO 인터페이스
package kr.co.sist.e_learning.admin.signup_req;

import java.util.List;

public interface AdminSignupRequestDAO {
    List<AdminSignupRequestDTO> listPendingRequests();
    AdminSignupRequestDTO getRequestById(String requestId);
    void approveRequest(AdminSignupRequestDTO dto);
    void rejectRequest(String requestId);
    List<String> getRolesByRequestId(String requestId);
    void deleteRequest(String requestId);
    void insertAdmin(AdminSignupRequestDTO dto);
    void insertRoles(String adminId, List<String> roleCodes);
}