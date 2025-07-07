package kr.co.sist.e_learning.community;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

public interface CommunityPostDAO {
    void insertPost(CommunityPostDTO dto);
    List<CommunityPostDTO> selectAllPosts();
    
    CommunityPostDTO selectPost(Long postId);
    
}
