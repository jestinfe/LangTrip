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
import java.sql.Timestamp; 
import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/dash/")
public class AdminCommuController {

    @Autowired
    private CommunityPostService communityPostService;

    @Autowired
    private VoteService voteService;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminCommuController.class);

    @GetMapping("/admincommunity")
    public String getAllPosts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "50") int size,
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "tab", defaultValue = "all") String tab,
        Model model) {

        List<CommunityPostDTO> posts;
        int totalPostCount;
        int noticeCount = 0; 
        int totalNoticesMatchingKeyword = 0; 
        int totalRegularPostsMatchingKeyword = 0; 

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
            totalNoticesMatchingKeyword = 0;
            totalRegularPostsMatchingKeyword = 0;
            noticeCount = 0;

        } else { // tab == "notice"
            posts = communityPostService.getNoticePosts(offset, size, keyword);
            posts.forEach(post -> post.setNickname("운영자"));
            totalPostCount = communityPostService.getTotalNoticePostCountWithSearch(keyword);
            totalNoticesMatchingKeyword = totalPostCount; 
            totalRegularPostsMatchingKeyword = 0;
            noticeCount = posts.size(); 
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

        model.addAttribute("noticeCount", noticeCount); 
        model.addAttribute("totalRegularPostsMatchingKeyword", totalRegularPostsMatchingKeyword); 
        model.addAttribute("totalNoticesMatchingKeyword", totalNoticesMatchingKeyword); 

        return "admin/dash/admincommunity"; // ⭐ 경로 수정
    }

    @GetMapping("/admincommunity/detail")
    public String getPostDetail(@RequestParam Long postId, Model model) {
        logger.debug("Received postId: {}", postId);
        CommunityPostDTO post = communityPostService.getPostDetail(postId); 
        
        if (post != null && "Y".equals(post.getCommunityNotice())) {
            post.setNickname("운영자"); 
        }
        
        if (post == null) {
        	logger.error("No post found with id: {}", postId);
        	model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
        	return "errorPage"; // 이 에러 페이지는 admin/dash 아래에 없을 수 있으니, 필요하면 경로를 조정해야 합니다.
        }
        
        List<CommunityCommentDTO> comments = communityPostService.getAllComments(postId); 
        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");
        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);
        logger.debug("Post details: {}", post);
        logger.debug("Total comments: {}", comments.size());
        return "admin/dash/admincommuDetail"; // ⭐ 경로 수정
    }
    
    @GetMapping("/commuSWrite")
    public String showAdminCommunityWritePage(Model model) {
        return "admin/dash/admincommuWrite"; // ⭐ 경로 수정
    }
    
    @PostMapping("/community/deletePost")
    public String deletePost(@RequestParam("postId") Long postId) {
        communityPostService.deletePostForAdmin(postId); 
        return "redirect:/adminDash/admincommunity"; 
    }

    @PostMapping("/community/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId, @RequestParam("postId") Long postId) {
        communityPostService.deleteCommentForAdmin(commentId); 
        return "redirect:/adminDash/admincommunity/detail?postId=" + postId; 
    }

    @PostMapping("/community/deleteReply")
    public String deleteReply(@RequestParam("replyId") Long replyId, @RequestParam("postId") Long postId) {
        communityPostService.deleteReplyForAdmin(replyId); 
        return "redirect:/adminDash/admincommunity/detail?postId=" + postId; 
    }
}
