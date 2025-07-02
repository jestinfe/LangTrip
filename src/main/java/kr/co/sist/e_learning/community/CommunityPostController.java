package kr.co.sist.e_learning.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/csj")
public class CommunityPostController {

    @GetMapping("/community")
    public String communityMain() {
        return "csj/community"; // templates/csj/community.html
    }
}
