package com.sparta.uandi.service;

import com.sparta.uandi.controller.request.EditMyInfoDto;
import com.sparta.uandi.controller.response.MemberResponseDto;
import com.sparta.uandi.controller.response.MyPostResponseDto;
import com.sparta.uandi.controller.response.PostResponseDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Participation;
import com.sparta.uandi.domain.Post;
import com.sparta.uandi.domain.UserDetailsImpl;
import com.sparta.uandi.repository.ParticipationRepository;
import com.sparta.uandi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final MemberService memberService;
    private final PostRepository postRepository;
    private final ParticipationRepository participationRepository;

    public ResponseDto<?> getMyInfo(UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .mbti(member.getMbti())
                        .writer(member.getWriter())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> editMyInfo(UserDetailsImpl userDetails, EditMyInfoDto editMyInfoDto) {
        Member member = userDetails.getMember();
        member.update(editMyInfoDto);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .mbti(member.getMbti())
                        .writer(member.getWriter())
                        .build()
        );
    }
    public ResponseDto<?> myPost(UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        List<Post> postList = postRepository.findAllByMemberId(memberId);
        if (postList.isEmpty()) {
            return ResponseDto.fail("NULL_POST", "쓴 게시물이 없습니다.");
        }
        return ResponseDto.success(postList);
    }

    public ResponseDto<?> myParticipation(UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        List<Participation> participationList = participationRepository.findAllByMemberId(memberId);
        if (participationList.isEmpty()) {
            return ResponseDto.fail("NULL_POST", "참여한 여행팟이 없습니다.");
        }
        List<Post> postList = new ArrayList<>();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for (Participation participation : participationList){
            postResponseDtoList.add(
                    PostResponseDto.builder()
                            .postId(participation.getPost().getPostId())
                            .title(participation.getPost().getTitle())
                            .city(participation.getPost().getCity())
                            .wrtier(participation.getPost().getMember().getWriter())
                            .mbti(participation.getPost().getMember().getMbti())
                            .departureDate(participation.getPost().getDepartureDate())
                            .arrivalDate(participation.getPost().getArrivalDate())
                            .content(participation.getPost().getContent())
                            .personnel(participation.getPost().getPersonnel())
                            .headCount(participationRepository.countByPostPostId(participation.getPost().getPostId()))
                            .imgUrl(participation.getPost().getImageUrl())
                            .createdAt(participation.getPost().getCreatedAt())
                            .modifiedAt(participation.getPost().getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(postList);
    }
}
