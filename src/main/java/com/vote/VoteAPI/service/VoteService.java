package com.vote.VoteAPI.service;

import com.vote.VoteAPI.model.Vote;
import com.vote.VoteAPI.repositories.ReviewRepository;
import com.vote.VoteAPI.repositories.VoteRepository;
import com.vote.VoteAPI.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReviewRepository reviewRepository;

    public boolean voteReviewApproved (Vote vote) throws IOException, InterruptedException {
        return reviewRepository.isApproved(vote.getReviewId());
    }


    public List<Vote> getAllVotes(){
        return repository.findAllVotes();
    }

    public int getTotalVotesByReviewId(Long reviewId){
        List<Vote> list = new ArrayList<>();
        list = repository.findId(reviewId);
        int sizeList = list.size();
        int votes = 0;
        for (int i=0; i<sizeList; i++){
            if(list.get(i).isVote()){
                votes++;
            }
        }
        return votes;
    }

    public Vote updateVoteReview(Vote vote){
        Long userId;
        try{
            userId = Long.valueOf(jwtUtils.getUserFromJwtToken(jwtUtils.getJwt()));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You are not logged");
        }
        Vote existVote = repository.findReviewIdAndUserId(vote.getReviewId(), userId);
        if(existVote == null){
            vote.setUserId(userId);
            return repository.save(vote);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You have already voted on this review");
        }
    }

    public Vote getVoteByReviewIdAndUserId(Long reviewId, Long userId){
        Vote existVote = repository.findReviewIdAndUserId(reviewId, userId);
        if(existVote != null){
            return existVote;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
