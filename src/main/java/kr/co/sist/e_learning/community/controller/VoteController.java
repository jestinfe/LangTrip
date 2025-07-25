package kr.co.sist.e_learning.community.controller;

import kr.co.sist.e_learning.community.dto.VoteDTO;
import kr.co.sist.e_learning.community.service.VoteService;
import kr.co.sist.e_learning.user.auth.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/csj")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody VoteDTO dto, Authentication authentication) {


        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
        }

        Long userSeq;
        try {
            userSeq = (Long) authentication.getPrincipal();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        int userId = userSeq.intValue();
        int postId = dto.getPostId();
        String type = dto.getVoteType();


        if (voteService.hasVotedToday(userId, postId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 오늘 투표함");
        }

        voteService.saveVote(userId, postId, type);

        int up = voteService.getVoteCount(postId, "UP");
        int down = voteService.getVoteCount(postId, "DOWN");


        return ResponseEntity.ok(Map.of("up", up, "down", down));
    }


}

