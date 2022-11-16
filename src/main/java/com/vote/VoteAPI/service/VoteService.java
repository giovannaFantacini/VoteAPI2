package com.vote.VoteAPI.service;

import com.vote.VoteAPI.model.Vote;
import com.vote.VoteAPI.repositories.ReviewRepository;
import com.vote.VoteAPI.repositories.Vote2Repository;
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

    @Autowired
    private Vote2Repository vote2Repository;

    public boolean voteReviewApproved (Vote vote) throws IOException, InterruptedException {
        return reviewRepository.isApproved(vote.getReviewId());
    }

    public int internalGetTotalVotesByReviewId(Long reviewId){
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


    public int getTotalVotesByReviewId(Long reviewId) throws IOException, InterruptedException {
        List<Vote> list = new ArrayList<>();
        int votesAPI2 = vote2Repository.getTotalVotesByReviewId(reviewId);
        list = repository.findId(reviewId);
        int sizeList = list.size();
        int votes = 0;
        for (int i=0; i<sizeList; i++){
            if(list.get(i).isVote()){
                votes++;
            }
        }
        votes = votes + votesAPI2;
        return votes;
    }

    public Vote updateVoteReview(Vote vote) throws IOException, InterruptedException {
        Long userId;
        try{
            userId = Long.valueOf(jwtUtils.getUserFromJwtToken(jwtUtils.getJwt()));
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You are not logged");
        }
        Vote existVote = repository.findReviewIdAndUserId(vote.getReviewId(), userId);
        boolean existVote2 = vote2Repository.existVote(vote.getReviewId(), userId);
        if(existVote == null && existVote2){
            vote.setUserId(userId);
            return repository.save(vote);
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You have already voted on this review");
        }
    }

    public Vote internalGetVoteByReviewIdAndUserId(Long reviewId, Long userId){
        Vote existVote = repository.findReviewIdAndUserId(reviewId, userId);
        if(existVote != null){
            return existVote;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Vote> getAllVotes(){
        return repository.findAllVotes();
    }

}
