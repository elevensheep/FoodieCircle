package com.example.map.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotBlank(message = "외부 식당 ID는 필수입니다")
    private String externalId;

    @NotBlank(message = "식당 이름은 필수입니다")
    private String restaurantName;

    private String address;
    private String category;

    @NotNull(message = "경도(x)는 필수입니다")
    private Double x;

    @NotNull(message = "위도(y)는 필수입니다")
    private Double y;

    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Size(max = 2000)
    private String content;

    @NotNull(message = "평점은 필수입니다")
    @Min(1) @Max(5)
    private Integer rating;

    @NotNull(message = "공개 범위는 필수입니다")
    private String visibility;

    private Long groupId;
}
