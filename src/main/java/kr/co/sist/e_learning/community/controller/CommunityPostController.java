package kr.co.sist.e_learning.community.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import kr.co.sist.e_learning.community.dto.PageDTO;
import kr.co.sist.e_learning.user.auth.UserAuthentication;
import kr.co.sist.e_learning.user.auth.UserEntity;
import kr.co.sist.e_learning.user.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.sist.e_learning.community.dto.CommunityCommentDTO;
import kr.co.sist.e_learning.community.dto.CommunityPostDTO;
import kr.co.sist.e_learning.community.service.CommunityPostService;
import kr.co.sist.e_learning.community.service.VoteService;

import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {

    

    @Autowired
    private CommunityPostService communityService;
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserRepository userRepository;

    // 현재 로그인된 사용자의 userSeq를 가져오는 헬퍼 메서드
    private Long getCurrentUserSeq() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
          
            return (Long) authentication.getPrincipal();
        }
      
        return null; // 로그인되지 않았거나 UserAuthentication 타입이 아닌 경우
    }

    // 현재 로그인된 사용자의 UserEntity를 가져오는 헬퍼 메서드
    private UserEntity getCurrentUserEntity() {
        Long userSeq = getCurrentUserSeq();
        if (userSeq != null) {
            UserEntity user = userRepository.findByUserSeq(userSeq).orElse(null);
          
            return user;
        }
        
        return null;
    }

    @GetMapping("/community")
    public String list(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "50") int size,
        @RequestParam(name = "tab", defaultValue = "all") String tab,
        @RequestParam(name = "keyword", required = false) String keyword,
        Model model
    ) {
        int offset = (page - 1) * size;

        List<CommunityPostDTO> postList;
        int totalCount;

        if ("best".equals(tab)) {
            postList = communityService.getBestPosts(offset, size);
            totalCount = communityService.getBestPostCount();
            model.addAttribute("bestPostCount", totalCount);
        } else {
            postList = communityService.getPostsPaginatedWithSearch(offset, size, keyword);
            totalCount = communityService.getTotalPostCountWithSearch(keyword);
        }

        int totalPages = (int) Math.ceil((double) totalCount / size);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("tab", tab);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPostCount", totalCount);

        return "csj/community";
    }

    @GetMapping("/communityWrite")
    public String communityWrite(Model model) { // Model 추가
        
        UserEntity currentUser = getCurrentUserEntity(); // UserEntity 가져오기
        if (currentUser == null) {
            
            return null;
        }
        model.addAttribute("currentUserNickname", currentUser.getNickname()); // 닉네임 추가
        model.addAttribute("currentUserSeq", currentUser.getUserSeq());     // userSeq 추가
       
        return "csj/communityWrite";
    }

    @PostMapping("/writeOk")
    public String writePost(CommunityPostDTO dto) {
        Long userSeq = getCurrentUserSeq();
        if (userSeq == null) {
            
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }
        dto.setUserId(userSeq); // CommunityPostDTO에 userId(userSeq) 설정
        communityService.writeRecommendation(dto);
        
        return "redirect:/csj/community";
    }

    @GetMapping("/community/detail")
    public String detail(@RequestParam("postId") Long postId, Model model) {
        
        Long currentUserSeq = getCurrentUserSeq();
        if (currentUserSeq == null) {
            
        } else {
            
        }

        communityService.increaseViewCount(postId);

        CommunityPostDTO post = communityService.getRecommendation(postId);
        List<CommunityCommentDTO> comments = communityService.getAllComments(postId);

        // 🔥 추천 수 조회 추가
        int upCount = voteService.getVoteCount(postId.intValue(), "UP");
        int downCount = voteService.getVoteCount(postId.intValue(), "DOWN");

        model.addAttribute("post", post);
        model.addAttribute("commentList", comments);
        model.addAttribute("upCount", upCount);
        model.addAttribute("downCount", downCount);
        model.addAttribute("currentUserSeq", currentUserSeq); // 현재 사용자 userSeq를 뷰에 전달

        
        return "csj/communityDetail";
    }

//댓글
    @GetMapping("/comment/add")
    public String commentAdd(@RequestParam("postId") Long postId, Model model) {
    	CommunityPostDTO post = communityService.getRecommendation(postId);
    	model.addAttribute("post", post);
    	return "csj/communityDetail";
    }

    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile imageFile) {
        
        if (getCurrentUserSeq() == null) {
            
            return "error: Not logged in"; // 또는 적절한 오류 처리
        }

        try {
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(dir, fileName);
            imageFile.transferTo(dest);

            
            return "/images/community/" + fileName;
        } catch (IOException e) {
           
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/community/delete")
    public String deletePost(@RequestParam("postId") Long postId) {
        
        // 권한 체크: 로그인한 유저와 작성자가 같을 때만 삭제 허용
        Long currentUserSeq = getCurrentUserSeq();
        if (currentUserSeq == null) {
            
            throw new IllegalStateException("로그인 상태가 아닙니다.");
        }

        CommunityPostDTO post = communityService.getRecommendation(postId);
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

        // 유저 정보 주입
        commentDTO.setUserId2(currentUser.getUserSeq());
        commentDTO.setNickname(currentUser.getNickname());

        // 방어 코드
        if (commentDTO.getPostId2() == null || commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            
            throw new IllegalArgumentException("postId2 또는 content가 비어 있음");
        }

        communityService.writeCommet(commentDTO);
        commentDTO.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
       
        return commentDTO;
    }

    @GetMapping("/csj/community")
    public String showCommunity(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String keyword,
            Model model) {

        PageDTO pageDTO = new PageDTO();
        pageDTO.setPage(page);
        pageDTO.setSize(size);
        pageDTO.setKeyword(keyword);

        List<CommunityPostDTO> postList = communityService.getPostList(pageDTO);
        int totalCount = communityService.getPostCount(pageDTO);

        model.addAttribute("postList", postList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int)Math.ceil((double)totalCount / size));
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "csj/community";
    }
}