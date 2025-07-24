package kr.co.sist.e_learning.community.service;

import java.util.List;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityImageDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.dto.PageDTO;

public interface CommunityPostService {

    // --- 일반 사용자용 메소드 (기존 메소드명 유지) ---
    // 게시글 작성
    void writePost(CommunityPostDTO dto);
    
    // 이미지 등록
    void insertImage(CommunityImageDTO dto);

    // 게시글 상세 조회
    CommunityPostDTO getPostDetail(Long postId); // 기존 메소드명 유지

    // 게시글 삭제
    int deletePost(Long postId); // 기존 메소드명 유지
    
    // 댓글 작성
    void writeComment(CommunityCommentDTO cDTO); // 기존 메소드명 유지
    
    // 댓글 목록 조회
    List<CommunityCommentDTO> getAllComments(Long postId); // 기존 메소드명 유지
    
    // 조회수 증가
    void increaseViewCount(Long postId); // 기존 메소드명 유지

    // 전체 게시글 조회
    List<CommunityPostDTO> getAllPosts();
    
    // 게시글 페이징 조회
    List<CommunityPostDTO> getPostsPaginated(int offset, int limit);
    
    // 전체 게시글 수 조회
    int getTotalPostCount();

    // 베스트 게시글 조회
    List<CommunityPostDTO> getBestPosts(int offset, int size);
    
    // 베스트 게시글 수 조회
    int getBestPostCount();

    // 게시글 리스트 (페이징)
    List<CommunityPostDTO> getPostList(PageDTO pageDTO);
    
    // 게시글 수 조회 (페이징)
    int getPostCount(PageDTO pageDTO);
    
    // 검색된 게시글 페이징 조회
    List<CommunityPostDTO> getPostsPaginatedWithSearch(int offset, int limit, String keyword);
    
    // 검색된 게시글 수 조회
    int getTotalPostCountWithSearch(String keyword);
    
    // 공지사항 목록 조회
    List<CommunityPostDTO> getNoticePosts(int offset, int size, String keyword);

    // 공지사항 전체 게시글 수 조회
    int getTotalNoticePostCountWithSearch(String keyword);

    // 일반 게시글 목록 조회
    List<CommunityPostDTO> getRegularPosts(int offset, int limit, String keyword);
    
    // 댓글 삭제
    int deleteComment(Long commentId); // 기존 메소드명 유지

    // 대댓글 삭제
    int deleteReply(Long replyId); // 기존 메소드명 유지
    
    // --- 관리자 전용 메소드 (새로운 메소드명 또는 관리자 기능에 특화된 메소드) ---
    
    // 관리자용 공지사항 작성
    void writeNoticeForAdmin(CommunityPostDTO dto); // 기존 메소드명 유지
    
    // 관리자용 게시글 삭제
    int deletePostForAdmin(Long postId); // 기존 메소드명 유지
    
    // 관리자용 댓글 삭제
    int deleteCommentForAdmin(Long commentId); // 기존 메소드명 유지
    
    // 관리자용 대댓글 삭제
    int deleteReplyForAdmin(Long replyId); // 기존 메소드명 유지
    
    // 특정 게시글의 모든 댓글 삭제 (관리자용)
    int deleteCommentsByPostId(Long postId); // 기존 메소드명 유지

    // 특정 게시글의 모든 좋아요(추천/비추천) 기록 삭제 (관리자용)
    int deleteLikesByPostId(Long postId); // 기존 메소드명 유지
    
    // 특정 게시글의 모든 이미지 기록 삭제 (관리자용)
    int deleteImagesByPostId(Long postId); // 기존 메소드명 유지

    // 관리자용 댓글 작성 메소드 선언 (새로 추가)
    void writeAdminComment(CommunityCommentDTO cDTO);
    
    int getTotalRegularPostCountWithSearch(String keyword);
}
