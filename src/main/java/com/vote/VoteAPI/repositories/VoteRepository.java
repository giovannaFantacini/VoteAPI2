package com.vote.VoteAPI.repositories;

import com.vote.VoteAPI.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.reviewId = :reviewId")
    Vote findId(Long reviewId);

    @Query("SELECT v FROM Vote v ")
    Vote findAllVotes();

    Vote save(Vote vote);
}
