package kr.co.sist.e_learning.admin.auth;

public interface AdminAuthDAO {
    AdminAuthDTO selectAdminByIdPw(String id, String pw);
}
