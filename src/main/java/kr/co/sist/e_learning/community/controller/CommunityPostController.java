	package kr.co.sist.e_learning.community.controller;

import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.service.CommunityPostService;
import kr.co.sist.e_learning.community.service.VoteService;
import kr.co.sist.e_learning.user.auth.UserAuthentication;
import kr.co.sist.e_learning.user.auth.UserRepository;
import kr.co.sist.e_learning.user.auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {


    @Autowired
    private CommunityPostService communityService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.upload-dir.root}")
    private String uploadDirRoot;

    @Value("${upload.path.community}")
    private String uploadPathWeb;

    private Long getCurrentUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return (Long) authentication.getPrincipal();
        }
        return null;
    }

    private UserEntity getCurrentUserEntity() {
        Long userSeq = getCurrentUserSeq();
        if (userSeq != null) {
            return userRepository.findByUserSeq(userSeq).orElse(null);
        }
        return null;
    }

    @GetMapping("/community")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "size", defaultValue = "50") int size,
                       @RequestParam(name = "tab", defaultValue = "all") String tab,
                       @RequestParam(name = "keyword", required = false) String keyword,
                       Model model) {

        int offset = (page - 1) * size;
        List<CommunityPostDTO> postList = new ArrayList<>();
        int totalCount = 0;
        int noticeCount = 0; // 현재 페이지에 표시될 공지사항 수
        int totalNoticesMatchingKeyword = 0; // 검색어에 맞는 전체 공지사항 수 (페이징 계산용)
        int totalRegularPostsMatchingKeyword = 0; // 검색어에 맞는 전체 일반 게시글 수 (페이징 계산용)

        if ("all".equals(tab)) {
            // ✅ 검색어에 따라 필터링된 공지사항 가져오기 (전체 공지사항 목록에서)
            List<CommunityPostDTO> noticePosts = communityService.getNoticePosts(0, Integer.MAX_VALUE, keyword);
            noticePosts.forEach(post -> post.setNickname("운영자"));
            postList.addAll(noticePosts);
            noticeCount = noticePosts.size(); // 현재 리스트에 포함된 공지사항의 실제 개수

            // ✅ 검색어에 따라 필터링된 일반 게시글의 전체 개수
            totalRegularPostsMatchingKeyword = communityService.getTotalPostCountWithSearch(keyword); // 일반 게시글은 getTotalPostCountWithSearch 사용
            
            // ✅ 검색어에 따라 필터링된 공지사항의 전체 개수 (페이징 계산용)
            totalNoticesMatchingKeyword = communityService.getTotalNoticePostCountWithSearch(keyword);

            List<CommunityPostDTO> regularPosts = communityService.getRegularPosts(offset, size, keyword);
            postList.addAll(regularPosts);
            
            // ✅ 전체 게시글 수 (페이징 계산용): 검색어에 맞는 공지사항 + 검색어에 맞는 일반 게시글
            totalCount = totalNoticesMatchingKeyword + totalRegularPostsMatchingKeyword;

            model.addAttribute("totalRegularPostsMatchingKeyword", totalRegularPostsMatchingKeyword); // 일반 게시글 전체 수 (번호 계산용)
            model.addAttribute("totalNoticesMatchingKeyword", totalNoticesMatchingKeyword); // 공지사항 전체 수 (번호 계산용)


        } else if ("best".equals(tab)) {
            postList = communityService.getBestPosts(offset, size);
            totalCount = communityService.getBestPostCount();

        } else if ("notice".equals(tab)) {
            postList = communityService.getNoticePosts(offset, size, keyword);
            postList.forEach(post -> post.setNickname("운영자"));
            totalCount = communityService.getTotalNoticePostCountWithSearch(keyword);
        }
        
        for (CommunityPostDTO post : postList) {
            long postId = post.getPostId();
            int upVoteCount = voteService.getVoteCount((int) postId, "UP");
            post.setUpCount(upVoteCount);
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("postList", postList);
        model.addAttribute("totalPostCount", totalCount);
        model.addAttribute("noticeCount", noticeCount); // 현재 리스트에 보이는 공지사항 수
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tab", tab);
        model.addAttribute("keyword", keyword);

        return "csj/community";
    }

    @GetMapping("/communityWrite")
    public String communityWrite(Model model) {
        UserEntity currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("currentUserNickname", currentUser.getNickname());
        model.addAttribute("currentUserSeq", currentUser.getUserSeq());
        return "csj/communityWrite";
    }

    @PostMapping("/writeOk")
    public String writePost(CommunityPostDTO dto) {
        Long userSeq = getCurrentUserSeq();
        if (userSeq == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        dto.setUserId(userSeq);
        communityService.writePost(dto);
        return "redirect:/csj/community";
    }

    @GetMapping("/community/detail")
    public String detail(@RequestParam("postId") Long postId, Model model) {
        Long currentUserSeq = getCurrentUserSeq();

        CommunityPostDTO post = communityService.getPostDetail(postId);
        communityService.increaseViewCount(postId);
        
        // 공지사항일 경우에만 닉네임을 "운영자"로 설정
        if (post != null && "Y".equals(post.getCommunityNotice())) {
            post.setNickname("운영자"); 
        }

        List<CommunityCommentDTO> comments = communityService.getAllComments(postId);
        
        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");

        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);
        model.addAttribute("currentUserSeq", currentUserSeq);
        return "csj/communityDetail";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile imageFile) {
        if (getCurrentUserSeq() == null) {
            return "error: Not logged in";
        }

        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dir = new File(uploadDirRoot + "/community");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);
            return uploadPathWeb + "/" + fileName;
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping("/community/delete")
    public String deletePost(@RequestParam("postId") Long postId) {
        Long currentUserSeq = getCurrentUserSeq();
        if (currentUserSeq == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        CommunityPostDTO post = communityService.getPostDetail(postId);
        if (post != null && currentUserSeq.equals(post.getUserId())) {
            communityService.deletePost(postId);
        } else {
            throw new IllegalStateException("삭제 권한이 없거나 게시글을 찾을 수 없습니다.");
        }
        return "redirect:/csj/community";
    }

    @PostMapping("/comment/write")
    @ResponseBody
    public CommunityCommentDTO writeComment(@RequestBody CommunityCommentDTO commentDTO) {
        UserEntity currentUser = getCurrentUserEntity();
        if (currentUser == null) {
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        commentDTO.setUserId2(currentUser.getUserSeq());
        commentDTO.setNickname(currentUser.getNickname());

        if (commentDTO.getPostId2() == null || commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("postId2 또는 content가 비어 있음");
        }

        communityService.writeComment(commentDTO);
        commentDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return commentDTO;
    }
}
