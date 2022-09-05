package com.sparta.uandi.controller;

import com.sparta.uandi.controller.request.CommentRequestDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @RequestMapping(value = "/api/auth/comment", method = RequestMethod.POST)
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(commentRequestDto, request);
    }

    // 댓글 목록
    @RequestMapping(value = "/api/comment/{postId}", method = RequestMethod.GET)
    public ResponseDto<?> getCommentListByPost(@PathVariable Long postId) {
        return commentService.getCommentListByPost(postId);
    }

    // 댓글 수정
    @RequestMapping(value = "/api/auth/comment/{commentId}", method = RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(commentId, commentRequestDto, request);
    }

    // 댓글 삭제
    @RequestMapping(value = "/api/auth/comment/{commentId}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.deleteComment(commentId, request);
    }

}
