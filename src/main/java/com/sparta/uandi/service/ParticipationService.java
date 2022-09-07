package com.sparta.uandi.service;

import com.sparta.uandi.controller.response.PostResponseDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Participation;
import com.sparta.uandi.domain.Post;
import com.sparta.uandi.jwt.TokenProvider;
import com.sparta.uandi.repository.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final PostService postService;
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    
    public ResponseDto<?> participate(Long postId, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Member member = memberService.validateMember(request);
        Post post = postService.isPresentPost(postId);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "유저가 존재하지 않습니다.");
        }
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        
        // 참가 한 기록이 없는 경우
        if (participationRepository.findByMemberAndPost(member, post).isEmpty()){
            participationRepository.save(new Participation(member, post));
            return ResponseDto.success(
                    PostResponseDto.builder()
                            .postId(post.getPostId())
                            .title(post.getTitle())
                            .city(post.getCity())
                            .wrtier(post.getMember().getWriter())
                            .mbti(post.getMember().getMbti())
                            .departureDate(post.getDepartureDate())
                            .arrivalDate(post.getArrivalDate())
                            .content(post.getContent())
                            .personnel(post.getPersonnel())
                            .headCount(participationRepository.countByPostPostId(postId))
                            .imgUrl(post.getImageUrl())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.fail("Already Exists", "이미 참가 신청을 하셨습니다.");
    }

    public ResponseDto<?> cancelToparticipate(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Member member = memberService.validateMember(request);
        Post post = postService.isPresentPost(postId);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "유저가 존재하지 않습니다.");
        }
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        if (!participationRepository.findByMemberAndPost(member, post).isEmpty()){
            participationRepository.delete(new Participation(member, post));
            return ResponseDto.success("Cancel likes success");
        }
        return ResponseDto.fail("NOT_ENROLLED", "참가 신청을 하지 않았습니다.");
    }
    
}
