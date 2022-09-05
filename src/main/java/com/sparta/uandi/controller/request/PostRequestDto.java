package com.sparta.uandi.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String city;
    private String title;
    private String departureDate;
    private String arrivalDate;
    private String content;
    private Long personnel;
    private MultipartFile imageFile;
}
