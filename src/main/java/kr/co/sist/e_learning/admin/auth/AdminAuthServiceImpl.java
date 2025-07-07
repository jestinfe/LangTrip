package kr.co.sist.e_learning.admin.auth;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

//	  Mapper 충돌 때문에 임시 주석
//    @Autowired
//    private AdminAuthDAO adminAuthDAO;

	
    @Override
    public AdminAuthDTO login(String id, String pw) {
//        
    	
//		Mapper 충돌 때문에 임시 return null; 
//    	return adminAuthDAO.selectAdminByIdPw(id, pw);
    	return null; 
    }
}
