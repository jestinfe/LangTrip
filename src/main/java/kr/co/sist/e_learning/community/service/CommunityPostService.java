package kr.co.sist.e_learning.community.service;

import java.util.List;

import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;


public interface CommunityPostService {

	void writeRecommendation(CommunityPostDTO dto);
	
    List<CommunityPostDTO> getAllPosts();
    
    CommunityPostDTO getRecommendation(Long postId);
	  
    void insertImage(CommunityImageDTO dto);
    
    void deletePost(Long postId);

    ///////////////////////////////////////////////
    
    void writeCommet(CommunityCommentDTO cDTO);
    
    List<CommunityCommentDTO> getAllComments(Long postId);
    
    List<CommunityPostDTO> getPostsPaginated(int offset, int limit);
    int getTotalPostCount();
    
    List<CommunityPostDTO> getBestPosts(int offset, int size);
    int getBestPostCount(); 
    
    void increaseViewCount(Long postId);
}