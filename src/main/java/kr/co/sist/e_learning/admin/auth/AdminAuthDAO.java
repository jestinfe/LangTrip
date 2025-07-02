package kr.co.sist.e_learning.admin.auth;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminAuthDAO {
    AdminAuthDTO selectAdminByIdPw(String id, String pw);
}
