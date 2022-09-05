package com.sparta.uandi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostResponseDto {
    private Long postId;
    private String title;
    private String city;
    private String wrtier;
    private String mbti;
    private String departureDate;
    private String arrivalDate;
    private String content;
    private Long personnel;
    private Long headCount;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentResponseDtoList;
    private List<ReviewResponseDto> reviewResponseDtoList;

}
