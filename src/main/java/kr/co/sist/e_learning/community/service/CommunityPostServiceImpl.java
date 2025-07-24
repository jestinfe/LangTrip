package kr.co.sist.e_learning.community.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import kr.co.sist.e_learning.community.dao.CommunityPostDAO;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

@Service
public class CommunityPostServiceImpl implements CommunityPostService {
	private static final Logger logger = LoggerFactory.getLogger(CommunityPostServiceImpl.class);

    @Autowired
    @Qualifier("communityPostDAOImpl")
    private CommunityPostDAO communityDAO;

    // --- 일반 사용자용 메소드 구현 ---

    @Override
    public void writePost(CommunityPostDTO dto) { // 인터페이스에 맞춤
        dto.setCommunityNotice("N"); // 일반 게시글은 'N'
        communityDAO.insertPost(dto);
    }

    @Override
    public void insertImage(CommunityImageDTO dto) { // 인터페이스에 맞춤
        communityDAO.insertImage(dto);
    }

    @Override
    public CommunityPostDTO getPostDetail(Long postId) { // 인터페이스에 맞춤
        communityDAO.increaseViewCount(postId);
        return communityDAO.selectPost(postId);
    }

    @Override
    public int deletePost(Long postId) { // 인터페이스에 맞춤
        // 사용자 페이지에서의 삭제는 soft delete (post_state = 'D')
        return communityDAO.deletePost(postId); 
    }
    
    @Override
    public void writeComment(CommunityCommentDTO cDTO) { // 인터페이스에 맞춤
        communityDAO.insertComment(cDTO);
    }

    @Override
    public List<CommunityCommentDTO> getAllComments(Long postId) { // 인터페이스에 맞춤
        return communityDAO.selectCommentList(postId);
    }

    @Override
    public void increaseViewCount(Long postId) { // 인터페이스에 맞춤
        communityDAO.increaseViewCount(postId);
    }

    @Override
    public List<CommunityPostDTO> getAllPosts() {
        return communityDAO.selectAllPosts();
    }

    @Override
    public List<CommunityPostDTO> getPostsPaginated(int offset, int limit) {
        return communityDAO.selectPostsPaginated(offset, limit);
    }

    @Override
    public int getTotalPostCount() {
        return communityDAO.selectTotalPostCount();
    }

    @Override
    public List<CommunityPostDTO> getBestPosts(int offset, int size) { // 인터페이스에 맞춤
        return communityDAO.selectBestPostsToday(offset, size);
    }

    @Override
    public int getBestPostCount() {
        return communityDAO.countBestPostsToday();
    }
    
    @Override
    public List<CommunityPostDTO> getPostList(PageDTO pageDTO) {
        return communityDAO.selectPostList(pageDTO);
    }

    @Override
    public int getPostCount(PageDTO pageDTO) {
        return communityDAO.selectPostCount(pageDTO);
    }
    
    @Override
    public List<CommunityPostDTO> getPostsPaginatedWithSearch(int offset, int limit, String keyword) {
        logger.debug("Fetching posts with offset: {}, limit: {}, keyword: {}", offset, limit, keyword);
        return communityDAO.selectPostsPaginatedWithSearch(offset, limit, keyword);
    }

    @Override
    public int getTotalPostCountWithSearch(String keyword) {
        return communityDAO.selectTotalPostCountWithSearch(keyword);
    }
    
    @Override
    public List<CommunityPostDTO> getNoticePosts(int offset, int size, String keyword) {
        return communityDAO.selectNoticePosts(offset, size, keyword);
    }

    @Override
    public int getTotalNoticePostCountWithSearch(String keyword) {
        // ✅ CommunityPostDAOImpl과 CommunityPostDAO 인터페이스의 메소드명에 맞게 변경
        return communityDAO.selectTotalNoticePostCountWithSearch(keyword); 
    }

    @Override
    public List<CommunityPostDTO> getRegularPosts(int offset, int limit, String keyword) {
        return communityDAO.selectRegularPosts(offset, limit, keyword);
    }
    
    @Override
    public int deleteComment(Long commentId) { // 인터페이스에 맞춤
        return communityDAO.deleteComment(commentId);
    }
    
    @Override
    public int deleteReply(Long replyId) { // 인터페이스에 맞춤
        return communityDAO.deleteReply(replyId);
    }
    
    // --- 관리자 전용 메소드 구현 ---

    @Override
    public void writeNoticeForAdmin(CommunityPostDTO dto) { // 인터페이스에 맞춤
        dto.setCommunityNotice("Y"); // 공지사항은 'Y'
        communityDAO.insertNoticePost(dto);
    }
    
    @Override
    public int deletePostForAdmin(Long postId) { // 인터페이스에 맞춤
        // 1. 해당 게시글에 속한 모든 댓글/대댓글 삭제
        communityDAO.deleteCommentsByPostId(postId);

        // 2. 해당 게시글에 속한 모든 좋아요/비추천 기록 삭제
        communityDAO.deleteLikesByPostId(postId);

        // 3. 해당 게시글에 속한 모든 이미지 기록 삭제
        communityDAO.deleteImagesByPostId(postId);

        // 4. 모든 자식 레코드 삭제가 완료된 후, 게시글 삭제 (Hard Delete)
        return communityDAO.deletePostForAdmin(postId);
    }
    
    @Override
    public int deleteCommentForAdmin(Long commentId) { // 인터페이스에 맞춤
        return communityDAO.deleteCommentForAdmin(commentId);
    }
    
    @Override
    public int deleteReplyForAdmin(Long replyId) { // 인터페이스에 맞춤
        return communityDAO.deleteReplyForAdmin(replyId);
    }

    @Override
    public int deleteCommentsByPostId(Long postId) { // 인터페이스에 맞춤
        return communityDAO.deleteCommentsByPostId(postId);
    }

    @Override
    public int deleteLikesByPostId(Long postId) { // 인터페이스에 맞춤
        return communityDAO.deleteLikesByPostId(postId);
    }
    
    @Override
    public int deleteImagesByPostId(Long postId) { // 인터페이스에 맞춤
        return communityDAO.deleteImagesByPostId(postId);
    }

    @Override
    public void writeAdminComment(CommunityCommentDTO cDTO) { // 인터페이스에 맞춤
        communityDAO.insertComment(cDTO); // 기존 댓글 삽입 DAO 메소드 재사용
    }
    
    @Override
    public int getTotalRegularPostCountWithSearch(String keyword) {
        return communityDAO.selectTotalRegularPostCountWithSearch(keyword);
    }
    
    
}
