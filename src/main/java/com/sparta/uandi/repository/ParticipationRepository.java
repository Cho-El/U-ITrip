package com.sparta.uandi.repository;

import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Participation;
import com.sparta.uandi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByMemberAndPost(Member Member, Post post);
//    Long countByMemberId(Long member_id);
    List<Participation> findAllByMemberId(Long memberId);
    Long countByPostPostId(Long post_id);
}
