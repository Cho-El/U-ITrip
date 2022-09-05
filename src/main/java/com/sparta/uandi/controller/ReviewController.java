package com.sparta.uandi.controller;

import com.sparta.uandi.controller.request.ReviewRequestDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @RequestMapping(value = "/api/auth/participation", method = RequestMethod.POST)
    public ResponseDto<?> createReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                        HttpServletRequest request) {
        return reviewService.createReview(reviewRequestDto, request);
    }

    // 리뷰 목록
    @RequestMapping(value = "/api/review/{postId}", method = RequestMethod.GET)
    public ResponseDto<?> getReviewListByPost(@PathVariable Long postId) {
        return reviewService.getReviewListByPost(postId);
    }

    // 리뷰 수정
    @RequestMapping(value = "/api/auth/review/{reviewId}", method = RequestMethod.PUT)
    public ResponseDto<?> updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto,
                                        HttpServletRequest request) {
        return reviewService.updateReview(reviewId, reviewRequestDto, request);
    }

    // 리뷰 삭제
    @RequestMapping(value = "/api/auth/review/{reviewId}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteReview(@PathVariable Long reviewId, HttpServletRequest request) {
        return reviewService.deleteReview(reviewId, request);
    }
}
