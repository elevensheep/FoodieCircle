import { Injectable } from '@nestjs/common';
import { ReviewRepository } from './review.repository';
import { RestaurantRepository } from '../restaurant/restaurant.repository';
import { CircleMemberRepository } from '../circle/circle-member.repository';
import { FileStorageService } from '../file/file-storage.service';
import { MarkerEventPublisher } from '../event/marker-event.publisher';
import { CreateReviewDto } from './dto/create-review.dto';
import { ReviewResponseDto } from './dto/review-response.dto';
import { Review, ReviewVisibility } from './review.entity';
import { ReviewImage } from './review-image.entity';
import { Restaurant } from '../restaurant/restaurant.entity';

@Injectable()
export class ReviewService {
  constructor(
    private readonly reviewRepository: ReviewRepository,
    private readonly restaurantRepository: RestaurantRepository,
    private readonly circleMemberRepository: CircleMemberRepository,
    private readonly fileStorageService: FileStorageService,
    private readonly markerEventPublisher: MarkerEventPublisher,
  ) {}

  async createReview(
    dto: CreateReviewDto,
    userId: number,
    images?: Express.Multer.File[],
  ): Promise<ReviewResponseDto> {
    let restaurant = await this.restaurantRepository.findByExternalId(dto.externalId);
    if (!restaurant) {
      const newRestaurant = new Restaurant();
      newRestaurant.externalId = dto.externalId;
      newRestaurant.name = dto.restaurantName;
      newRestaurant.address = dto.address;
      newRestaurant.category = dto.category;
      newRestaurant.x = dto.x;
      newRestaurant.y = dto.y;
      restaurant = await this.restaurantRepository.save(newRestaurant);
    }

    const review = new Review();
    review.restaurant = restaurant;
    review.userId = userId;
    review.content = dto.content;
    review.rating = dto.rating;
    review.visibility = dto.visibility as ReviewVisibility;
    review.groupId = dto.groupId;
    review.images = [];

    if (images) {
      for (const image of images) {
        const imagePath = await this.fileStorageService.store(image);
        const reviewImage = new ReviewImage();
        reviewImage.imagePath = imagePath;
        review.images.push(reviewImage);
      }
    }

    const saved = await this.reviewRepository.save(review);

    await this.markerEventPublisher.publish({
      reviewId: saved.id,
      userId,
      restaurantName: restaurant.name,
      externalId: restaurant.externalId,
      groupId: dto.groupId,
    });

    const savedWithRelations = await this.reviewRepository.findOneWithRelations(saved.id);
    return ReviewResponseDto.from(savedWithRelations);
  }

  async getFeed(groupId: number, page: number, size: number): Promise<ReviewResponseDto[]> {
    const members = await this.circleMemberRepository.findByCircleId(groupId);
    const userIds = members.map((m) => m.userId);

    const reviews = await this.reviewRepository.findByUserIdsAndGroupId(
      userIds,
      groupId,
      page * size,
      size,
    );
    return reviews.map(ReviewResponseDto.from);
  }
}
