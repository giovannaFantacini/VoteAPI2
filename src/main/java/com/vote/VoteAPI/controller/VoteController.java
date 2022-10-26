package com.vote.VoteAPI.controller;

import com.vote.VoteAPI.model.Vote;
import com.vote.VoteAPI.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/votes")
public class VoteController {
    @Autowired
    private VoteService service;

    @PostMapping(value = "/updateVote")
    public Vote upVoteReview(@RequestBody final Vote vote ) throws IOException, InterruptedException {
        /*Review review = reviewService.findOne(vote.getReviewId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Review Not Found"));
        boolean status = reviewService.goodToVote(review);
        if (status) {
            boolean haveVoted = service.updateVoteReview(vote);
            if (haveVoted) {
                reviewService.updateVotes(vote, review);
                return ResponseEntity.ok("Vote changed");
            } else
                throw new ResponseStatusException(HttpStatus.CONFLICT,"You have already voted on this review");
        }else
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This review isn't approved yet");*/
        return service.updateVoteReview(vote);
    }

    @GetMapping(value = "/")
    public List<Vote> getAllVotes(){
        return service.getAllVotes();
    }

    @GetMapping(value = "/{reviewId}")
    public int getTotalVotesByReviewId(@PathVariable("reviewId") final Long reviewId ){
        return service.getTotalVotesByReviewId(reviewId);
    }


}
