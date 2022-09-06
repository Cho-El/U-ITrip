package com.sparta.uandi.controller.response;

import com.sparta.uandi.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPostResponseDto {
    private List<Post> postList;
}
