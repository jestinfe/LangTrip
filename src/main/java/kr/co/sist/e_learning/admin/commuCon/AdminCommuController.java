package kr.co.sist.e_learning.admin.commuCon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kr.co.sist.e_learning.community.service.CommunityPostService;
import kr.co.sist.e_learning.community.service.VoteService;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.user.auth.UserAuthentication; // UserAuthentication import 추가
import org.springframework.security.core.Authentication; // Authentication import 추가
import org.springframework.security.core.context.SecurityContextHolder; // SecurityContextHolder import 추가

import java.sql.Timestamp; 
import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/dash") // ⭐ 이 부분이 /admin/dash 로 다시 변경되었습니다.
public class AdminCommuController {

    @Autowired
    private CommunityPostService communityPostService;

    @Autowired
    private VoteService voteService;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminCommuController.class);

    // ⭐ 추가: 현재 로그인된 사용자의 userSeq를 가져오는 헬퍼 메소드
    private Long getCurrentUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/admincommunity")
    public String getAllPosts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "50") int size,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "tab", defaultValue = "all") String tab,
        Model model) {

        List<CommunityPostDTO> posts;
        int totalPostCount;
        int noticeCount = 0; // 현재 페이지에 표시될 공지사항 수
        int totalNoticesMatchingKeyword = 0; // 검색어에 맞는 전체 공지사항 수 (페이징 계산용)
        int totalRegularPostsMatchingKeyword = 0; // 검색어에 맞는 전체 일반 게시글 수 (페이징 계산용)

        int offset = (page - 1) * size; 

        if ("all".equals(tab) || tab == null) {
            List<CommunityPostDTO> noticePosts = communityPostService.getNoticePosts(0, Integer.MAX_VALUE, keyword);
            noticePosts.forEach(post -> post.setNickname("운영자"));
            
            totalRegularPostsMatchingKeyword = communityPostService.getTotalPostCountWithSearch(keyword); 
            totalNoticesMatchingKeyword = communityPostService.getTotalNoticePostCountWithSearch(keyword);

            List<CommunityPostDTO> regularPosts = communityPostService.getRegularPosts(offset, size, keyword);
            
            posts = new ArrayList<>();
            posts.addAll(noticePosts); 
            posts.addAll(regularPosts); 
            
            noticeCount = noticePosts.size(); 
            
            totalPostCount = totalNoticesMatchingKeyword + totalRegularPostsMatchingKeyword;

        } else if ("best".equals(tab)) {
            posts = communityPostService.getBestPosts(offset, size);
            totalPostCount = communityPostService.getBestPostCount();
            // best 탭일 때도 notice 관련 변수들을 0으로 초기화하여 모델에 추가
            totalNoticesMatchingKeyword = 0;
            totalRegularPostsMatchingKeyword = 0;
            noticeCount = 0;

        } else { // tab == "notice"
            posts = communityPostService.getNoticePosts(offset, size, keyword);
            posts.forEach(post -> post.setNickname("운영자"));
            totalPostCount = communityPostService.getTotalNoticePostCountWithSearch(keyword);
            // notice 탭일 때도 regular 관련 변수들을 0으로 초기화하여 모델에 추가
            totalNoticesMatchingKeyword = totalPostCount; // notice 탭에서는 totalPostCount가 곧 totalNoticesMatchingKeyword
            totalRegularPostsMatchingKeyword = 0;
            noticeCount = posts.size(); // 현재 페이지에 보이는 공지사항 수
        }
        
        for (CommunityPostDTO post : posts) {
            long postId = post.getPostId();
            int upVoteCount = voteService.getVoteCount((int) postId, "UP");
            post.setUpCount(upVoteCount);
        }
        
        int totalPages = (int) Math.ceil((double) totalPostCount / size);
        
        model.addAttribute("posts", posts);
        model.addAttribute("totalPostCount", totalPostCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("tab", tab);
        model.addAttribute("keyword", keyword); 

        // 모든 경우에 noticeCount, totalRegularPostsMatchingKeyword, totalNoticesMatchingKeyword를 모델에 추가
        model.addAttribute("noticeCount", noticeCount); 
        model.addAttribute("totalRegularPostsMatchingKeyword", totalRegularPostsMatchingKeyword); 
        model.addAttribute("totalNoticesMatchingKeyword", totalNoticesMatchingKeyword); 
        
        // ⭐ 추가: 현재 로그인된 사용자 ID를 모델에 추가
        model.addAttribute("currentUserSeq", getCurrentUserSeq());

        return "admin/dash/admincommunity"; 
    }

    @GetMapping("/admincommunity/detail")
    public String getPostDetail(@RequestParam Long postId, Model model) {
        logger.debug("Received postId: {}", postId);
        CommunityPostDTO post = communityPostService.getPostDetail(postId); 
        
        // 공지사항일 경우에만 닉네임을 "운영자"로 설정합니다.
        // 일반 게시글의 경우, community.xml의 selectPost 쿼리에서 가져온 실제 닉네임이 그대로 사용됩니다.
        if (post != null && "Y".equals(post.getCommunityNotice())) {
            post.setNickname("운영자"); 
        }
        
        if (post == null) {
        	logger.error("No post found with id: {}", postId);
        	model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
        	return "errorPage";
        }
        
        List<CommunityCommentDTO> comments = communityPostService.getAllComments(postId); 
        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");
        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);
        
        // ⭐ 추가: 현재 로그인된 사용자 ID를 모델에 추가
        model.addAttribute("currentUserSeq", getCurrentUserSeq());

        logger.debug("Post details: {}", post);
        logger.debug("Total comments: {}", comments.size());
        return "admin/dash/admincommuDetail"; 
    }
    
    // 관리자는 글쓰기 페이지를 볼 수 있지만, 실제 글 작성은 여기서 처리하지 않음 (POST 엔드포인트 제거)
    @GetMapping("/commuSWrite")
    public String showAdminCommunityWritePage(Model model) {
        // ⭐ 추가: 현재 로그인된 사용자 ID를 모델에 추가 (글쓰기 페이지에서도 필요할 수 있음)
        model.addAttribute("currentUserSeq", getCurrentUserSeq());
        return "admin/dash/admincommuWrite"; 
    }
    
    // 게시글 삭제 POST 엔드포인트는 유지 (관리자가 삭제는 가능)
    @PostMapping("/community/deletePost")
    public String deletePost(@RequestParam("postId") Long postId) {
        communityPostService.deletePostForAdmin(postId); 
        return "redirect:/admin/dash/admincommunity"; // ⭐ 경로 수정
    }

    // 댓글 삭제 POST 엔드포인트는 유지 (관리자가 삭제는 가능)
    @PostMapping("/community/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId, @RequestParam("postId") Long postId) {
        communityPostService.deleteCommentForAdmin(commentId); 
        return "redirect:/admin/dash/admincommunity/detail?postId=" + postId; // ⭐ 경로 수정
    }

    // 대댓글 삭제 POST 엔드포인트는 유지 (관리자가 삭제는 가능)
    @PostMapping("/community/deleteReply")
    public String deleteReply(@RequestParam("replyId") Long replyId, @RequestParam("postId") Long postId) {
        communityPostService.deleteReplyForAdmin(replyId); 
        return "redirect:/admin/dash/admincommunity/detail?postId=" + postId; // ⭐ 경로 수정
    }
}
