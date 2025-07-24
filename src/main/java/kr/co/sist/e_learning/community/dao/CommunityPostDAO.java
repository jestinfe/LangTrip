package kr.co.sist.e_learning.community.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

@Mapper // 또는 @Repository
public interface CommunityPostDAO {
    void insertPost(CommunityPostDTO dto);
    List<CommunityPostDTO> selectAllPosts();
    CommunityPostDTO selectPost(Long postId);
    void insertImage(CommunityImageDTO dto);
    
    // 게시글 삭제 (반환 타입을 int로 통일)
    int deletePost(Long postId);
    
    int countAllPosts();
    List<CommunityCommentDTO> selectCommentList(Long postId);
    void insertComment(CommunityCommentDTO cDTO);
    List<CommunityPostDTO> selectPostsPaginated(int offset, int limit);
    int selectTotalPostCount();
    List<CommunityPostDTO> selectBestPostsToday(@Param("offset") int offset, @Param("limit") int limit);
    int countBestPostsToday();
    void increaseViewCount(Long postId);
    List<CommunityPostDTO> selectPostList(PageDTO pageDTO);
    int selectPostCount(PageDTO pageDTO);
    List<CommunityPostDTO> selectPostsPaginatedWithSearch(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);
    int selectTotalPostCountWithSearch(String keyword);
    List<CommunityPostDTO> selectNoticePosts(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);
    
    // ✅ 메소드명 변경: getTotalNoticePostCountWithSearch -> selectTotalNoticePostCountWithSearch
    // 이 메소드명이 CommunityPostDAOImpl과 MyBatis XML 매퍼에 있는 이름과 일치해야 합니다.
    int selectTotalNoticePostCountWithSearch(String keyword); 
    
    List<CommunityPostDTO> selectRegularPosts(@Param("offset") int offset, @Param("limit") int limit, @Param("keyword") String keyword);
    void insertNoticePost(CommunityPostDTO dto);
    
    // 댓글 삭제 (반환 타입을 int로 통일)
    int deleteComment(Long commentId);
    int deleteReply(Long replyId);

    // 관리자용 메소드 (반환 타입을 int로 통일)
    int deletePostForAdmin(Long postId);
    int deleteCommentForAdmin(Long commentId);
    int deleteReplyForAdmin(Long replyId);
    
    // 특정 게시글의 모든 댓글 삭제
    int deleteCommentsByPostId(Long postId);

    // 특정 게시글의 모든 좋아요(추천/비추천) 기록 삭제
    int deleteLikesByPostId(Long postId);
    
    // 특정 게시글의 모든 이미지 기록 삭제
    int deleteImagesByPostId(Long postId);

    // 관리자용 댓글 작성 메소드 추가
    void writeAdminComment(CommunityCommentDTO cDTO);
    
    // ✅ 추가: 검색어에 따른 일반 게시글 전체 수 조회
    int selectTotalRegularPostCountWithSearch(String keyword);
    
}
