package com.sparta.uandi.domain;

import com.sparta.uandi.controller.request.ReviewRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Review extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // 리뷰 남길 게시글
    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    // 작성자 정보
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 리뷰 내용
    @Column(nullable = false)
    private String content;


    // 작성자 확인
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    // 리뷰 수정
    public void update(ReviewRequestDto reviewRequestDto) {
        this.content = reviewRequestDto.getContent();
    }
}
