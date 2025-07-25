package kr.co.sist.e_learning.community.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

@Repository
@Primary
public class CommunityPostDAOImpl implements CommunityPostDAO {

    private final SqlSessionTemplate session;
    private final String NS = "kr.co.sist.e_learning.community.dao.CommunityPostDAO";

    @Autowired
    public CommunityPostDAOImpl(SqlSessionTemplate session) {
        this.session = session;
    }

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

    @Override
    public void insertImage(CommunityImageDTO dto) {
        session.insert(NS + ".insertImage", dto);
    }

    @Override
    public int deletePost(Long postId) {
        return session.delete(NS + ".deletePost", postId);
    }

    @Override
    public int countAllPosts() {
        return session.selectOne(NS + ".countAllPosts");
    }
    
    @Override
    public List<CommunityCommentDTO> selectCommentList(Long postId) {
        return session.selectList(NS+".selectCommentList",postId);
    }
    
    @Override
    public void insertComment(CommunityCommentDTO cDTO) {
        session.insert(NS+".insertComment",cDTO);
    }
    
    @Override
    public int deleteComment(Long commentId) {
        return session.delete(NS + ".deleteComment", commentId);
    }
    
    @Override
    public int deleteReply(Long replyId) {
        return session.delete(NS + ".deleteReply", replyId);
    }

    @Override
    public List<CommunityPostDTO> selectPostsPaginated(int offset, int limit) {
        Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("offset", offset);
        paramMap.put("limit", limit);
        return session.selectList(NS + ".selectPostsPaginated", paramMap);
    }
    
    @Override
    public int selectTotalPostCount() {
        return session.selectOne(NS + ".selectTotalPostCount");
    }
    
    @Override
    public List<CommunityPostDTO> selectBestPostsToday(int offset, int limit) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);
        return session.selectList(NS + ".selectBestPostsToday", param);
    }
    
    @Override
    public int countBestPostsToday() {
        return session.selectOne(NS + ".countBestPostsToday");
    }
    
    @Override
    public void increaseViewCount(Long postId) {
        session.update(NS + ".increaseViewCount", postId);
    }
    
    @Override
    public List<CommunityPostDTO> selectPostList(PageDTO pageDTO) {
        return session.selectList("csjMapper.selectPostList", pageDTO);
    }

    @Override
    public int selectPostCount(PageDTO pageDTO) {
        return session.selectOne("csjMapper.selectPostCount", pageDTO);
    }

    @Override
    public List<CommunityPostDTO> selectPostsPaginatedWithSearch(int offset, int limit, String keyword) {
        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);
        param.put("keyword", keyword);
        return session.selectList(NS+".selectPostsPaginatedWithSearch", param);
    }

    @Override
    public int selectTotalPostCountWithSearch(String keyword) {
        return session.selectOne(NS+".selectTotalPostCountWithSearch", keyword);
    }

    @Override
    public List<CommunityPostDTO> selectNoticePosts(int offset, int limit, String keyword) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", offset);
        paramMap.put("limit", limit);
        paramMap.put("keyword", keyword);
        return session.selectList(NS + ".selectNoticePosts", paramMap);
    }

    @Override
    public int selectTotalNoticePostCountWithSearch(String keyword) {
        return session.selectOne(NS + ".selectTotalNoticePostCountWithSearch", keyword);
    }
    
    @Override
    public List<CommunityPostDTO> selectRegularPosts(int offset, int limit, String keyword) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("offset", offset);
        paramMap.put("limit", limit);
        paramMap.put("keyword", keyword);
        return session.selectList(NS + ".selectRegularPosts", paramMap);
    }
    
    // ✅ 추가: selectTotalRegularPostCountWithSearch 메소드 구현
    @Override
    public int selectTotalRegularPostCountWithSearch(String keyword) {
        return session.selectOne(NS + ".selectTotalRegularPostCountWithSearch", keyword);
    }

    @Override
    public void insertNoticePost(CommunityPostDTO dto) {
        session.insert(NS + ".insertNoticePost", dto);
    }
    
    @Override
    public int deletePostForAdmin(Long postId) {
        return session.delete(NS + ".deletePostForAdmin", postId);
    }

    @Override
    public int deleteCommentForAdmin(Long commentId) {
        return session.delete(NS + ".deleteCommentForAdmin", commentId);
    }

    @Override
    public int deleteReplyForAdmin(Long replyId) {
        return session.delete(NS + ".deleteReplyForAdmin", replyId);
    }
    
    @Override
    public int deleteCommentsByPostId(Long postId) {
        return session.delete(NS + ".deleteCommentsByPostId", postId);
    }
    
    @Override
    public int deleteLikesByPostId(Long postId) {
        return session.delete(NS + ".deleteLikesByPostId", postId);
    }
    
    @Override
    public int deleteImagesByPostId(Long postId) {
        return session.delete(NS + ".deleteImagesByPostId", postId);
    }

    @Override
    public void writeAdminComment(CommunityCommentDTO cDTO) {
        session.insert(NS + ".insertComment", cDTO); // 기존 댓글 삽입 DAO 메소드 재사용
    }
}
