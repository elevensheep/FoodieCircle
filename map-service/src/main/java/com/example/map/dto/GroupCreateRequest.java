package com.example.map.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupCreateRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<Long> memberIds;
}
