import { Test, TestingModule } from '@nestjs/testing';
import { ReviewController } from './review.controller';
import { ReviewService } from './review.service';
import { ReviewResponseDto } from './dto/review-response.dto';

describe('ReviewController', () => {
  let controller: ReviewController;

  const mockReviewService = {
    createReview: jest.fn(),
    getFeed: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [ReviewController],
      providers: [{ provide: ReviewService, useValue: mockReviewService }],
    }).compile();

    controller = module.get<ReviewController>(ReviewController);
  });

  afterEach(() => jest.clearAllMocks());

  it('createReview_리뷰_등록_성공', async () => {
    const dto = {
      externalId: 'kakao-123',
      restaurantName: '맛있는 식당',
      x: 127.0,
      y: 37.5,
      content: '정말 맛있어요',
      rating: 5,
      visibility: 'PUBLIC',
    } as any;
    const response: ReviewResponseDto = {
      id: 1,
      restaurantName: '맛있는 식당',
      externalId: 'kakao-123',
      userId: 1,
      content: '정말 맛있어요',
      rating: 5,
      visibility: 'PUBLIC',
      imagePaths: [],
      createdAt: new Date(),
    };
    mockReviewService.createReview.mockResolvedValue(response);

    const result = await controller.createReview(dto, undefined, '1');

    expect(result.status).toBe(200);
    expect(result.message).toBe('리뷰 등록 성공');
    expect(result.data.restaurantName).toBe('맛있는 식당');
    expect(mockReviewService.createReview).toHaveBeenCalledWith(dto, 1, undefined);
  });

  it('getFeed_피드_조회_성공', async () => {
    const response: ReviewResponseDto[] = [
      {
        id: 1,
        restaurantName: '맛있는 식당',
        externalId: 'kakao-123',
        userId: 2,
        content: '맛있어요',
        rating: 4,
        visibility: 'GROUP',
        imagePaths: [],
        createdAt: new Date(),
      },
    ];
    mockReviewService.getFeed.mockResolvedValue(response);

    const result = await controller.getFeed(1, 0, 10);

    expect(result.status).toBe(200);
    expect(result.message).toBe('피드 조회 성공');
    expect(result.data).toHaveLength(1);
    expect(result.data[0].restaurantName).toBe('맛있는 식당');
  });
});
