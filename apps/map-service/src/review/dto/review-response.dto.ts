import { Review } from '../review.entity';

export class ReviewResponseDto {
  id: number;
  restaurantName: string;
  externalId: string;
  userId: number;
  content: string;
  rating: number;
  visibility: string;
  imagePaths: string[];
  createdAt: Date;

  static from(review: Review): ReviewResponseDto {
    const dto = new ReviewResponseDto();
    dto.id = review.id;
    dto.restaurantName = review.restaurant.name;
    dto.externalId = review.restaurant.externalId;
    dto.userId = review.userId;
    dto.content = review.content;
    dto.rating = review.rating;
    dto.visibility = review.visibility;
    dto.imagePaths = review.images?.map((img) => img.imagePath) ?? [];
    dto.createdAt = review.createdAt;
    return dto;
  }
}
