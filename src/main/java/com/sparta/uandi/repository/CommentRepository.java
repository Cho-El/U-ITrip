package com.sparta.uandi.repository;

import com.sparta.uandi.domain.Comment;
import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    void deleteByPostPostId(Long postId);
}