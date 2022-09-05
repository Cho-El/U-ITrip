package com.sparta.uandi.controller;

import com.sparta.uandi.controller.request.PostRequestDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @RequestMapping(value = "/api/auth/post", method = RequestMethod.POST)
    public ResponseDto<?> createPost(@ModelAttribute PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {
        return postService.createPost(postRequestDto, request);
    }

    // 게시글 전체 목록
    @RequestMapping(value = "/api/post", method = RequestMethod.GET)
    public ResponseDto<?> getPostList() {
        return postService.getPostList();
    }

    // 게시글 상세 조회
    @RequestMapping(value = "/api/post/{postId}", method = RequestMethod.GET)
    public ResponseDto<?> getPostDetail(@PathVariable Long postId) {
        return postService.getPostDetail(postId);
    }

    // 게시글 수정
    @RequestMapping(value = "/api/auth/post/{postId}", method = RequestMethod.PUT)
    public ResponseDto<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(postId, postRequestDto, request);
    }

    // 게시글 삭제
    @RequestMapping(value = "/api/auth/post/{postId}", method = RequestMethod.DELETE)
    public ResponseDto<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        return postService.deletePost(postId, request);
    }
}
