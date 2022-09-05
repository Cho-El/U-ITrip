package com.sparta.uandi.repository;

import com.sparta.uandi.domain.Post;
import com.sparta.uandi.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByPost(Post post);

    void deleteByPostPostId(Long postId);
}
