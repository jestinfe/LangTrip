package kr.co.sist.e_learning.admin.signup_req;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminSignupRequestDAOImpl implements AdminSignupRequestDAO {

    @Autowired
    private SqlSession sqlSession;
    private final String NS = "kr.co.sist.e_learning.admin.account.AdminSignupRequestMapper.";

    @Override
    public List<AdminSignupRequestDTO> listPendingRequests() {
        return sqlSession.selectList(NS + "listPendingRequests");
    }

    @Override
    public AdminSignupRequestDTO getRequestById(String requestId) {
        return sqlSession.selectOne(NS + "getRequestById", requestId);
    }

    @Override
    public void approveRequest(AdminSignupRequestDTO dto) {
        sqlSession.update(NS + "updateStatusApproved", dto.getRequestId());
    }

    @Override
    public void rejectRequest(String requestId) {
        sqlSession.update(NS + "updateStatusRejected", requestId);
    }

    @Override
    public List<String> getRolesByRequestId(String requestId) {
        return sqlSession.selectList(NS + "getRolesByRequestId", requestId);
    }

    @Override
    public void deleteRequest(String requestId) {
        sqlSession.delete(NS + "deleteSignupRole", requestId); // 순서 주의: role 먼저
        sqlSession.delete(NS + "deleteSignupRequest", requestId);
    }


    @Override
    public void insertAdmin(AdminSignupRequestDTO dto) {
    	int result = sqlSession.insert("kr.co.sist.e_learning.admin.account.AdminSignupRequestMapper.insertAdmin", dto);
        System.out.println("관리사 승인 결과 int"+result);
    	if (result == 0) {
            throw new IllegalStateException("관리자 등록에 실패했습니다. adminId=" + dto.getAdminId());
        }
    }

    @Override
    public void insertRoles(String adminId, List<String> roleCodes) {
        for (String code : roleCodes) {
            Map<String, String> param = new HashMap<>();
            param.put("adminId", adminId);
            param.put("roleCode", code);
            sqlSession.insert(NS + "insertAdminRole", param);
        }
    }
    
    
}