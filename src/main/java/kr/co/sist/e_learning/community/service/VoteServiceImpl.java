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
        return result > 0;
    }

    @Override
    public void saveVote(Integer userId, Integer postId, String voteType) {
        voteDAO.saveVote(userId, postId, voteType);
    }

    @Override
    public int getVoteCount(Integer postId, String voteType) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("voteType", voteType);
        int count = voteDAO.countVotes(params);
        return count;
    }
}
