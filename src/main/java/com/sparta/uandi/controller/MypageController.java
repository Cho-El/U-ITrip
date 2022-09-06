package com.sparta.uandi.controller;

import com.sparta.uandi.controller.request.EditMyInfoDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.domain.UserDetailsImpl;
import com.sparta.uandi.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class MypageController {
    private final MypageService mypageService;
    @GetMapping("/api/auth/member")
    public ResponseDto<?> getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.getMyInfo(userDetails);
    }
    @PutMapping ("/api/auth/member")
    public ResponseDto<?> editMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody EditMyInfoDto editMyInfoDto){
        return mypageService.editMyInfo(userDetails, editMyInfoDto);
    }
    @GetMapping("/api/auth/myPost")
    public ResponseDto<?> myPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.myPost(userDetails);
    }

    @GetMapping("/api/auth/myparticipation")
    public ResponseDto<?> myParticipation(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return mypageService.myParticipation(userDetails);
    }

}
