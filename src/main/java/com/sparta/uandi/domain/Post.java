package com.sparta.uandi.domain;

import com.sparta.uandi.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    // 작성자 정보
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // 여행갈 지역
    @Column(nullable = false)
    private String city;

    // 게시글 제목
    @Column(nullable = false)
    private String title;

    // 여행 출발 날짜
    @Column(nullable = false)
    private String departureDate;

    // 여행 도착 날짜
    @Column(nullable = false)
    private String arrivalDate;

    // 게시글 내용
    @Column(nullable = false)
    private String content;

    // 최대 인원 수
    @Column(nullable = false)
    private Long personnel;

    // 이미지
    @Column()
    private String imageUrl;

    // 댓글 리스트
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;

    // 리뷰 리스트
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviewList;

    // 작성자 확인
    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    // 게시글 수정
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.departureDate = postRequestDto.getDepartureDate();
        this.arrivalDate = postRequestDto.getArrivalDate();
        this.content = postRequestDto.getContent();
        this.personnel = postRequestDto.getPersonnel();
    }
}
