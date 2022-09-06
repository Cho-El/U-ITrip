package com.sparta.uandi.controller;

import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class ParticipationController {
    private final ParticipationService participationService;

    @PostMapping("/api/auth/participation/{postId}")
    public ResponseDto<?> participate(@PathVariable Long postId, HttpServletRequest request){
        return participationService.participate(postId, request);
    }

    @DeleteMapping("/api/auth/participation/{postId}")
    public ResponseDto<?> cancelToParticipate(@PathVariable Long postId, HttpServletRequest request){
        return participationService.cancelToparticipate(postId, request);
    }
}
