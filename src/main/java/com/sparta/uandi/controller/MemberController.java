package com.sparta.uandi.controller;

import com.sparta.uandi.controller.request.LoginRequestDto;
import com.sparta.uandi.controller.request.MemberRequestDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/member/signup")
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto){
        return memberService.createMember(requestDto);
    }

    @PostMapping("/api/member/login")
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto)
        return memberService.login(requestDto, response);

    @
}