package kr.co.sist.e_learning.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityPostServiceImpl implements CommunityPostService {

    @Autowired
    private CommunityPostDAO communityDAO;

    @Override
    public List<CommunityPostDTO> getAllPosts() {
        return communityDAO.selectAllPosts();
    }

    @Override
    public void writeRecommendation(CommunityPostDTO dto) {
        communityDAO.insertPost(dto);
    }
    
    @Override
    public CommunityPostDTO getRecommendation(Long postId) {
        return communityDAO.selectPost(postId);
    }
    
}

