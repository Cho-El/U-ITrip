package com.sparta.uandi.repository;

import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByModifiedAtDesc();
    Optional<Post> findByMemberId(Member member);
    List<Post> findByMember(Member member);
}
