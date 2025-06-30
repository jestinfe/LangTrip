package kr.co.sist.e_learning.admin.auth;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminAuthDAOImpl implements AdminAuthDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public AdminAuthDTO selectAdminByIdPw(String id, String pw) {
    	return sqlSession.selectOne("adminAuth.selectAdminByIdPw", Map.of("id", id, "pw", pw));
    }
}
