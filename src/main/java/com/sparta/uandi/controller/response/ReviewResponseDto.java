package com.sparta.uandi.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
