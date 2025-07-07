package kr.co.sist.e_learning.admin.auth;

<<<<<<< HEAD
//import org.springframework.beans.factory.annotation.Autowired;
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
>>>>>>> branch 'dev' of https://github.com/tjdwo00/e_learning_prj.git
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

//	  Mapper 충돌 때문에 임시 주석
//    @Autowired
//    private AdminAuthDAO adminAuthDAO;

	
    @Override
    public AdminAuthDTO login(String id, String pw) {
<<<<<<< HEAD
//        
    	
//		Mapper 충돌 때문에 임시 return null; 
//    	return adminAuthDAO.selectAdminByIdPw(id, pw);
    	return null; 
=======

        AdminAuthDTO admin = adminAuthDAO.loginSelectAdminById(id);

        if (admin == null || !"Y".equals(admin.getStatus())) {
            return null;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(pw, admin.getAdminPw())) {
            return admin; // ✅ 로그인 성공
        }

        return null; // ❌ 비밀번호 틀림
>>>>>>> branch 'dev' of https://github.com/tjdwo00/e_learning_prj.git
    }
}
