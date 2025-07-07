package kr.co.sist.e_learning.admin.auth;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminAuthDAO {
    AdminAuthDTO loginSelectAdminById(String id);
}
