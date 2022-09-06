package com.sparta.uandi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long postId;
    private Long userId;
}
