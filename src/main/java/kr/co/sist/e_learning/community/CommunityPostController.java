package kr.co.sist.e_learning.community;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityService;
    
    @GetMapping("/community")
    public String list(Model model) {
        List<CommunityPostDTO> postList = communityService.getAllPosts();

        model.addAttribute("postList", postList);
        return "csj/community";
    }

    // 글쓰기 폼으로 이동
    @GetMapping("/communityWrite")
    public String communityWrite(HttpSession session) {
    	if (session.getAttribute("loginUser") == null) {
            UserDTO fakeUser = new UserDTO();
            fakeUser.setUserSeq("user001"); // DB에 있는 유저 ID (예시)
            fakeUser.setEmail("user1@example.com"); // 필요하면 이메일도
            fakeUser.setNickname("닉네임1");
            session.setAttribute("loginUser", fakeUser);
        }
    	
        return "csj/communityWrite";
    }

    @PostMapping("/writeOk")
    public String writePost(CommunityPostDTO dto) {
        communityService.writeRecommendation(dto);
        return "redirect:/csj/community"; 
    }
    
    @GetMapping("/community/detail")
    public String detail(@RequestParam("postId") Long postId, Model model) {
        CommunityPostDTO post = communityService.getRecommendation(postId);
        model.addAttribute("post", post);
        return "csj/communityDetail"; 
    }
    
    
}