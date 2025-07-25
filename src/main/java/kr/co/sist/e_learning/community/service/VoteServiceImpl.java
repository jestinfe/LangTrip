package kr.co.sist.e_learning.community.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.sist.e_learning.community.dao.VoteDAO;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteDAO voteDAO;

    @Override
    public boolean hasVotedToday(Integer userId, Integer postId) {
        int result = voteDAO.hasVotedToday(userId, postId);
        System.out.println("📌 hasVotedToday() 호출됨 - userId: " + userId + ", postId: " + postId + " → 결과: " + result);
        return result > 0;
    }

    @Override
    public void saveVote(Integer userId, Integer postId, String voteType) {
        System.out.println("💾 saveVote() 호출됨 - userId: " + userId + ", postId: " + postId + ", type: " + voteType);
        voteDAO.saveVote(userId, postId, voteType);
        System.out.println("✅ saveVote() 완료됨");
    }

    @Override
    public int getVoteCount(Integer postId, String voteType) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("voteType", voteType);
        int count = voteDAO.countVotes(params);
        System.out.println("📈 getVoteCount() - postId: " + postId + ", type: " + voteType + " → count: " + count);
        return count;
    }
}
