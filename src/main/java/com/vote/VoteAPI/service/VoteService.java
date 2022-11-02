package com.vote.VoteAPI.service;

import com.vote.VoteAPI.model.Vote;
import com.vote.VoteAPI.repositories.VoteRepository;
import com.vote.VoteAPI.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;

    @Autowired
    private JwtUtils jwtUtils;

    public boolean voteReviewApproved (Vote vote) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8081/reviews/status/" + vote.getReviewId()))
                .build();

        HttpResponse response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        var code = response.statusCode();
        if(code == 200){
            String statusReview = response.body().toString();
            if(statusReview.equals("APPROVED")) {
                return true;
            }
        }
        return false;
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
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You are logged");
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

}
