package com.vote.VoteAPI.repositories;

import com.vote.VoteAPI.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.reviewId = :reviewId")
    List<Vote> findId(Long reviewId);

    @Query("SELECT v FROM Vote v ")
    List<Vote> findAllVotes();

    Vote save(Vote vote);


}
