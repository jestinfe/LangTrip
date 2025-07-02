package kr.co.sist.e_learning.admin.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminSignupDAOImpl implements AdminSignupDAO {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Override
    public int insertAdmin(AdminSignupDTO dto) {
        return sqlSession.insert("AdminSignupMapper.insertAdmin", dto);
    }

    @Override
    public void insertPermission(String adminId, String permission) {
        Map<String, String> param = new HashMap<>();
        param.put("adminId", adminId);  // 🔄 key 변경 (adminSeq ❌ → adminId ✅)
        param.put("permissionCode", permission);
        sqlSession.insert("AdminSignupMapper.insertPermission", param); // ✅ 네임스페이스도 맞춤
    }

    
    @Override
    public void insertVerificationCode(EmailVerificationDTO dto) {
        sqlSession.insert("kr.co.sist.e_learning.admin.auth.AdminAuthDAO.insertVerificationCode", dto);
    }

    @Override
    public EmailVerificationDTO selectValidVerification(String email, String code) {
        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("code", code);
        return sqlSession.selectOne("kr.co.sist.e_learning.admin.auth.AdminAuthDAO.selectValidVerification", param);
    }

    @Override
    public void markCodeVerified(String verificationSeq) {
        sqlSession.update("kr.co.sist.e_learning.admin.auth.AdminAuthDAO.markCodeVerified", verificationSeq);
    }


		
}

