import { Injectable } from '@nestjs/common';
import { KakaoLocalApiClient } from './kakao-local-api.client';
import { ReviewRepository } from '../review/review.repository';
import { RestaurantSearchResponseDto } from './dto/restaurant-search-response.dto';

@Injectable()
export class RestaurantSearchService {
  constructor(
    private readonly kakaoLocalApiClient: KakaoLocalApiClient,
    private readonly reviewRepository: ReviewRepository,
  ) {}

  async search(
    keyword: string,
    x: number,
    y: number,
    radius: number,
  ): Promise<RestaurantSearchResponseDto[]> {
    const places = await this.kakaoLocalApiClient.searchByKeyword(keyword, x, y, radius);

    return Promise.all(
      places.map(async (place) => ({
        externalId: place.id,
        name: place.placeName,
        address: place.addressName,
        category: place.categoryName,
        x: parseFloat(place.x),
        y: parseFloat(place.y),
        reviewCount: await this.reviewRepository.countByRestaurantExternalId(place.id),
      })),
    );
  }
}
