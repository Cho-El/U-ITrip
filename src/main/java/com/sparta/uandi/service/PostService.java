package com.sparta.uandi.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.uandi.controller.request.PostRequestDto;
import com.sparta.uandi.controller.response.CommentResponseDto;
import com.sparta.uandi.controller.response.PostResponseDto;
import com.sparta.uandi.controller.response.ResponseDto;
import com.sparta.uandi.controller.response.ReviewResponseDto;
import com.sparta.uandi.domain.Comment;
import com.sparta.uandi.domain.Member;
import com.sparta.uandi.domain.Post;
import com.sparta.uandi.domain.Review;
import com.sparta.uandi.jwt.TokenProvider;
import com.sparta.uandi.repository.CommentRepository;
import com.sparta.uandi.repository.PostRepository;
import com.sparta.uandi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    @Value("${cloud.aws.s3.bucket}")
    private String innocampbucket;
    private final AmazonS3Client amazonS3Client;
    private final TokenProvider tokenProvider;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;


    // 게시글 생성
    @Transactional
    public ResponseDto<?> createPost(PostRequestDto postRequestDto, HttpServletRequest request) throws IOException {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        String imageUrl = "";
        if (!postRequestDto.getImageFile().isEmpty()) {
            imageUrl = uploadImage(postRequestDto);
        }

        Post post = Post.builder()
                .member(member)
                .city(postRequestDto.getCity())
                .title(postRequestDto.getTitle())
                .departureDate(postRequestDto.getDepartureDate())
                .arrivalDate(postRequestDto.getArrivalDate())
                .content(postRequestDto.getContent())
                .personnel(postRequestDto.getPersonnel())
                .imageUrl(imageUrl)
                .build();

        postRepository.save(post);

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
                        // headCount
                        .imgUrl(post.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 게시글 전체 목록
    @Transactional(readOnly = true)
    public ResponseDto<?> getPostList() {
        return ResponseDto.success(postRepository.findAllByOrderByModifiedAtDesc());
    }

    // 게시글 상세조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getPostDetail(Long postId) {

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        // 게시글 상세조회 : 댓글
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .commentId(comment.getCommentId())
                            .writer(comment.getMember().getWriter())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        // 게시글 상세조회 : 리뷰
        List<Review> reviewList = reviewRepository.findAllByPost(post);
        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            reviewResponseDtoList.add(
                    ReviewResponseDto.builder()
                            .reviewId(review.getReviewId())
                            .writer(review.getMember().getWriter())
                            .content(review.getContent())
                            .createdAt(review.getCreatedAt())
                            .modifiedAt(review.getModifiedAt())
                            .build()
            );
        }

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
                        // headCount
                        .imgUrl(post.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .commentResponseDtoList(commentResponseDtoList)
                        .reviewResponseDtoList(reviewResponseDtoList)
                        .build()
        );
    }

    // 게시글 수정
    @Transactional
    public ResponseDto<Post> updatePost(Long postId, PostRequestDto postRequestDto, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        post.update(postRequestDto);
        return ResponseDto.success(post);
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long postId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Post post = isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
        }

        if (post.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        commentRepository.deleteByPostPostId(postId); // 해당 게시글 댓글 삭제
        reviewRepository.deleteByPostPostId(postId); // 해당 게시글 리뷰 삭제
        postRepository.delete(post);

        return ResponseDto.success("delete success");
    }

    // 멤버 인증
    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }

    // 게시글 존재 여부
    @Transactional(readOnly = true)
    public Post isPresentPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElse(null);
    }

    public String uploadImage(PostRequestDto fileDto) throws IOException {
        MultipartFile file = fileDto.getImageFile();
        String originalName = file.getOriginalFilename(); // 파일명
        long size = file.getSize(); // 파일 크기

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(file.getContentType()); // 다운로드가 아닌 브라우저로 이미지 조회 가능
        objectMetaData.setContentLength(size); // 설정하지 않으면 업로드는 되나, 에러 발생

        // PutObjectRequest 는 Aws S3 버킷에 업로드할 객체 메타 데이터와 파일 데이터로 이루어져있다.
        // S3 에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(innocampbucket, originalName, file.getInputStream(), objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // 접근가능한 URL 가져오기
        String url = amazonS3Client.getUrl(innocampbucket, originalName).toString();

        // 삭제 객체 생성
        DeleteObjectRequest request = new DeleteObjectRequest(innocampbucket, originalName);
        amazonS3Client.deleteObject(request);

        return url;
    }
}
