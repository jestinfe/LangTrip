package kr.co.sist.e_learning.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.mybatis.spring.SqlSessionTemplate;

@Repository
public class CommunityPostDAOImpl implements CommunityPostDAO {

    @Autowired
    private SqlSessionTemplate session;

    private final String NS = "kr.co.sist.e_learning.community.CommunityPostDAO";

    @Override
    public void insertPost(CommunityPostDTO dto) {
        session.insert(NS + ".insertPost", dto);
    }

    @Override
    public List<CommunityPostDTO> selectAllPosts() {
        return session.selectList(NS + ".selectAllPosts");
    }
    
    @Override
    public CommunityPostDTO selectPost(Long postId) {
        return session.selectOne(NS + ".selectPost", postId);
    }
}

