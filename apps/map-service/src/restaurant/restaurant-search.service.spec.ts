import { Test, TestingModule } from '@nestjs/testing';
import { RestaurantSearchService } from './restaurant-search.service';
import { KakaoLocalApiClient } from './kakao-local-api.client';
import { ReviewRepository } from '../review/review.repository';
import { KakaoPlaceDto } from './dto/kakao-place.dto';

describe('RestaurantSearchService', () => {
  let service: RestaurantSearchService;

  const mockKakaoApiClient = {
    searchByKeyword: jest.fn(),
  };

  const mockReviewRepository = {
    countByRestaurantExternalId: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        RestaurantSearchService,
        { provide: KakaoLocalApiClient, useValue: mockKakaoApiClient },
        { provide: ReviewRepository, useValue: mockReviewRepository },
      ],
    }).compile();

    service = module.get<RestaurantSearchService>(RestaurantSearchService);
  });

  afterEach(() => jest.clearAllMocks());

  it('search_카카오API_결과에_리뷰_수_포함하여_반환', async () => {
    const places: KakaoPlaceDto[] = [
      {
        id: 'kakao-123',
        placeName: '맛있는 식당',
        addressName: '서울시 강남구',
        categoryName: '한식',
        x: '127.0',
        y: '37.5',
      },
    ];
    mockKakaoApiClient.searchByKeyword.mockResolvedValue(places);
    mockReviewRepository.countByRestaurantExternalId.mockResolvedValue(5);

    const result = await service.search('한식', 127.0, 37.5, 1000);

    expect(result).toHaveLength(1);
    expect(result[0].externalId).toBe('kakao-123');
    expect(result[0].name).toBe('맛있는 식당');
    expect(result[0].x).toBe(127.0);
    expect(result[0].y).toBe(37.5);
    expect(result[0].reviewCount).toBe(5);
    expect(mockKakaoApiClient.searchByKeyword).toHaveBeenCalledWith('한식', 127.0, 37.5, 1000);
  });

  it('search_카카오API_결과_없으면_빈_배열_반환', async () => {
    mockKakaoApiClient.searchByKeyword.mockResolvedValue([]);

    const result = await service.search('없는식당', 127.0, 37.5, 1000);

    expect(result).toHaveLength(0);
    expect(mockReviewRepository.countByRestaurantExternalId).not.toHaveBeenCalled();
  });
});
