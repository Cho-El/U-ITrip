package com.sparta.uandi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.uandi.controller.request.EditMyInfoDto;
import com.sparta.uandi.controller.request.MemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    private String mbti;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    public void update(EditMyInfoDto requestDto){
        mbti = requestDto.getMbti();
        writer = requestDto.getWriter();
        if (!mbti.isBlank()){
            this.mbti = mbti;
        }
        if(!writer.isBlank()) {
            this.writer = writer;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return id != null && Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}