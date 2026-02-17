package com.example.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedPageResponse {

    private List<FeedItemResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static FeedPageResponse from(Page<FeedItemResponse> page) {
        return new FeedPageResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
