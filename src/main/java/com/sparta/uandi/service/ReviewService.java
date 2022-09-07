package com.sparta.uandi.service;

import com.sparta.uandi.controller.request.ReviewRequestDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.controller.response.ReviewResponseDto;
import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Post;
import com.sparta.uandi.domain.Review;
import com.sparta.uandi.jwt.TokenProvider;
import com.sparta.uandi.repository.ParticipationRepository;
import com.sparta.uandi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ParticipationRepository participationRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;

    // 리뷰 생성
    @Transactional
    public ResponseDto<?> createReview(ReviewRequestDto reviewRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(reviewRequestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        // 참여자인지 확인
        if (participationRepository.findByMemberAndPost(member, post).isEmpty()) {
            return ResponseDto.fail("BAD_REQUEST", "참여자만 작성할 수 있습니다.");
        }

        Review review = Review.builder()
                .post(post)
                .member(member)
                .content(reviewRequestDto.getContent())
                .build();

        reviewRepository.save(review);

        return ResponseDto.success(
                ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .writer(review.getMember().getWriter())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .modifiedAt(review.getModifiedAt())
                        .build()
        );
    }

    // 리뷰 목록
    @Transactional(readOnly = true)
    public ResponseDto<?> getReviewListByPost(Long postId) {

        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        List<Review> reviewList = reviewRepository.findAllByPost(post);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .writer(review.getMember().getWriter())
                            .content(review.getContent())
                            .createdAt(review.getCreatedAt())
                            .modifiedAt(review.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(reviewResponseDtoList);
    }

    // 리뷰 수정
    @Transactional
    public ResponseDto<?> updateReview(Long reviewId, ReviewRequestDto reviewRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = postService.isPresentPost(reviewRequestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        Review review = isPresentReview(reviewId);
        if (null == review) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 리뷰 입니다.");
        }

        if (review.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        review.update(reviewRequestDto);

        return ResponseDto.success(
                ReviewResponseDto.builder()
                        .reviewId(review.getReviewId())
                        .writer(review.getMember().getWriter())
                        .content(review.getContent())
                        .createdAt(review.getCreatedAt())
                        .modifiedAt(review.getModifiedAt())
                        .build()
        );
    }

    // 리뷰 삭제
    @Transactional
    public ResponseDto<?> deleteReview(Long reviewId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Review review = isPresentReview(reviewId);
        if (null == review) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 리뷰 입니다.");
        }

        if (review.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);

        return ResponseDto.success("delete success");
    }

    // 멤버 인증
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 리뷰 존재 여부
    @Transactional(readOnly = true)
    public Review isPresentReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        return optionalReview.orElse(null);
    }
}
