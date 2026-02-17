package com.example.map.service;

import com.example.map.dto.ReviewCreateRequest;
import com.example.map.dto.ReviewResponse;
import com.example.map.entity.Restaurant;
import com.example.map.entity.Review;
import com.example.map.entity.ReviewImage;
import com.example.map.event.MarkerCreatedEvent;
import com.example.map.event.MarkerEventPublisher;
import com.example.map.repository.CircleMemberRepository;
import com.example.map.repository.RestaurantRepository;
import com.example.map.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final CircleMemberRepository circleMemberRepository;
    private final FileStorageService fileStorageService;
    private final MarkerEventPublisher markerEventPublisher;

    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest request, Long userId, List<MultipartFile> images) throws IOException {
        Restaurant restaurant = restaurantRepository.findByExternalId(request.getExternalId())
                .orElseGet(() -> restaurantRepository.save(
                        Restaurant.create(
                                request.getExternalId(),
                                request.getRestaurantName(),
                                request.getAddress(),
                                request.getCategory(),
                                request.getX(),
                                request.getY()
                        )
                ));

        Review review = Review.create(
                restaurant,
                userId,
                request.getContent(),
                request.getRating(),
                Review.Visibility.valueOf(request.getVisibility()),
                request.getGroupId()
        );

        if (images != null) {
            for (MultipartFile image : images) {
                String path = fileStorageService.store(image);
                review.addImage(ReviewImage.create(review, path));
            }
        }

        Review saved = reviewRepository.save(review);

        markerEventPublisher.publish(new MarkerCreatedEvent(
                saved.getId(),
                userId,
                restaurant.getName(),
                restaurant.getExternalId(),
                request.getGroupId()
        ));

        return ReviewResponse.from(saved);
    }

    public Page<ReviewResponse> getFeed(Long groupId, Pageable pageable) {
        List<Long> memberUserIds = circleMemberRepository.findByCircleId(groupId).stream()
                .map(m -> m.getUserId())
                .toList();

        return reviewRepository.findByUserIdInAndGroupIdOrderByCreatedAtDesc(memberUserIds, groupId, pageable)
                .map(ReviewResponse::from);
    }
}
