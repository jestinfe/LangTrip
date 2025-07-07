package kr.co.sist.e_learning.community;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface CommunityPostService {

	void writeRecommendation(CommunityPostDTO dto);
	
    List<CommunityPostDTO> getAllPosts();
    
    CommunityPostDTO getRecommendation(Long postId);
	  
	  
}
