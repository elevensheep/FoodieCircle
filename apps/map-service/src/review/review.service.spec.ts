import { Test, TestingModule } from '@nestjs/testing';
import { ReviewService } from './review.service';
import { ReviewRepository } from './review.repository';
import { RestaurantRepository } from '../restaurant/restaurant.repository';
import { CircleMemberRepository } from '../circle/circle-member.repository';
import { FileStorageService } from '../file/file-storage.service';
import { MarkerEventPublisher } from '../event/marker-event.publisher';
import { Review, ReviewVisibility } from './review.entity';
import { Restaurant } from '../restaurant/restaurant.entity';
import { CircleMember } from '../circle/circle-member.entity';

describe('ReviewService', () => {
  let service: ReviewService;

  const mockReviewRepository = {
    save: jest.fn(),
    findOneWithRelations: jest.fn(),
    findByUserIdsAndGroupId: jest.fn(),
  };

  const mockRestaurantRepository = {
    findByExternalId: jest.fn(),
    save: jest.fn(),
  };

  const mockCircleMemberRepository = {
    findByCircleId: jest.fn(),
  };

  const mockFileStorageService = {
    store: jest.fn(),
  };

  const mockMarkerEventPublisher = {
    publish: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        ReviewService,
        { provide: ReviewRepository, useValue: mockReviewRepository },
        { provide: RestaurantRepository, useValue: mockRestaurantRepository },
        { provide: CircleMemberRepository, useValue: mockCircleMemberRepository },
        { provide: FileStorageService, useValue: mockFileStorageService },
        { provide: MarkerEventPublisher, useValue: mockMarkerEventPublisher },
      ],
    }).compile();

    service = module.get<ReviewService>(ReviewService);
  });

  afterEach(() => jest.clearAllMocks());

  const baseDto = {
    externalId: 'kakao-123',
    restaurantName: '맛있는 식당',
    address: '서울시 강남구',
    category: '한식',
    x: 127.0,
    y: 37.5,
    content: '정말 맛있어요',
    rating: 5,
    visibility: 'PUBLIC',
    groupId: undefined,
  } as any;

  it('createReview_기존_식당_리뷰_등록_성공', async () => {
    const restaurant = Object.assign(new Restaurant(), {
      id: 1, externalId: 'kakao-123', name: '맛있는 식당',
    });
    const savedReview = Object.assign(new Review(), {
      id: 1, restaurant, userId: 1, content: '정말 맛있어요',
      rating: 5, visibility: ReviewVisibility.PUBLIC, images: [], createdAt: new Date(),
    });

    mockRestaurantRepository.findByExternalId.mockResolvedValue(restaurant);
    mockReviewRepository.save.mockResolvedValue(savedReview);
    mockReviewRepository.findOneWithRelations.mockResolvedValue(savedReview);
    mockMarkerEventPublisher.publish.mockResolvedValue(undefined);

    const result = await service.createReview(baseDto, 1);

    expect(result.restaurantName).toBe('맛있는 식당');
    expect(result.rating).toBe(5);
    expect(mockRestaurantRepository.save).not.toHaveBeenCalled();
    expect(mockReviewRepository.save).toHaveBeenCalled();
    expect(mockMarkerEventPublisher.publish).toHaveBeenCalled();
  });

  it('createReview_새_식당_생성_후_리뷰_등록', async () => {
    const newRestaurant = Object.assign(new Restaurant(), {
      id: 2, externalId: 'kakao-456', name: '새 식당',
    });
    const savedReview = Object.assign(new Review(), {
      id: 2, restaurant: newRestaurant, userId: 1, content: '좋아요',
      rating: 4, visibility: ReviewVisibility.PUBLIC, images: [], createdAt: new Date(),
    });

    mockRestaurantRepository.findByExternalId.mockResolvedValue(null);
    mockRestaurantRepository.save.mockResolvedValue(newRestaurant);
    mockReviewRepository.save.mockResolvedValue(savedReview);
    mockReviewRepository.findOneWithRelations.mockResolvedValue(savedReview);
    mockMarkerEventPublisher.publish.mockResolvedValue(undefined);

    const result = await service.createReview({ ...baseDto, externalId: 'kakao-456', restaurantName: '새 식당', content: '좋아요', rating: 4 }, 1);

    expect(mockRestaurantRepository.save).toHaveBeenCalled();
    expect(result.restaurantName).toBe('새 식당');
  });

  it('getFeed_그룹_멤버_피드_조회', async () => {
    const member = Object.assign(new CircleMember(), { userId: 2 });
    const restaurant = Object.assign(new Restaurant(), { name: '맛있는 식당', externalId: 'kakao-123' });
    const review = Object.assign(new Review(), {
      id: 1, restaurant, userId: 2, content: '좋아요', rating: 4,
      visibility: ReviewVisibility.GROUP, images: [], createdAt: new Date(),
    });

    mockCircleMemberRepository.findByCircleId.mockResolvedValue([member]);
    mockReviewRepository.findByUserIdsAndGroupId.mockResolvedValue([review]);

    const result = await service.getFeed(1, 0, 10);

    expect(result).toHaveLength(1);
    expect(result[0].userId).toBe(2);
    expect(mockReviewRepository.findByUserIdsAndGroupId).toHaveBeenCalledWith([2], 1, 0, 10);
  });
});
