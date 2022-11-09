package com.vote.VoteAPI.controller;

import com.vote.VoteAPI.model.Vote;
import com.vote.VoteAPI.security.JwtRequestFilter;
import com.vote.VoteAPI.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private VoteService service;

    @PostMapping(value = "/updateVote")
    public Vote upVoteReview(@RequestBody final Vote vote ) throws IOException, InterruptedException {
        boolean voteReviewApproved = service.voteReviewApproved(vote);
        if(voteReviewApproved){
            return service.updateVoteReview(vote);
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This review isn't approved yet");
        }
    }

    @GetMapping(value = "/")
    public List<Vote> getAllVotes(){return service.getAllVotes();}


    @GetMapping(value = "/{reviewId}")
    public int getTotalVotesByReviewId(@PathVariable("reviewId") final Long reviewId ){
        return service.getTotalVotesByReviewId(reviewId);
    }

    @GetMapping(value = "/{reviewId}/{userId}")
    public Vote getVoteByReviewIdAndUserId(@PathVariable("reviewId") final Long reviewId, @PathVariable("userId") final Long userId ){
        return service.getVoteByReviewIdAndUserId(reviewId,userId);

    }

}
