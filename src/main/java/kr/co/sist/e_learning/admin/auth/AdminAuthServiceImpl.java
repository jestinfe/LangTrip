package kr.co.sist.e_learning.admin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

    @Autowired
    private AdminAuthDAO adminAuthDAO;

    @Override
    public AdminAuthDTO login(String id, String pw) {
        return adminAuthDAO.selectAdminByIdPw(id, pw);
    }
}
