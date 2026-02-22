import { Test, TestingModule } from '@nestjs/testing';
import { RestaurantSearchController } from './restaurant-search.controller';
import { RestaurantSearchService } from './restaurant-search.service';
import { RestaurantSearchResponseDto } from './dto/restaurant-search-response.dto';

describe('RestaurantSearchController', () => {
  let controller: RestaurantSearchController;

  const mockSearchService = {
    search: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [RestaurantSearchController],
      providers: [{ provide: RestaurantSearchService, useValue: mockSearchService }],
    }).compile();

    controller = module.get<RestaurantSearchController>(RestaurantSearchController);
  });

  afterEach(() => jest.clearAllMocks());

  it('search_식당_검색_성공', async () => {
    const results: RestaurantSearchResponseDto[] = [
      {
        externalId: 'kakao-123',
        name: '맛있는 식당',
        address: '서울시 강남구',
        category: '한식',
        x: 127.0,
        y: 37.5,
        reviewCount: 3,
      },
    ];
    mockSearchService.search.mockResolvedValue(results);

    const result = await controller.search('한식', 127.0, 37.5, 1000);

    expect(result.status).toBe(200);
    expect(result.message).toBe('식당 검색 성공');
    expect(result.data).toHaveLength(1);
    expect(result.data[0].name).toBe('맛있는 식당');
    expect(result.data[0].reviewCount).toBe(3);
    expect(mockSearchService.search).toHaveBeenCalledWith('한식', 127.0, 37.5, 1000);
  });

  it('search_기본_반경_1000m_적용', async () => {
    mockSearchService.search.mockResolvedValue([]);

    const result = await controller.search('카페', 127.0, 37.5, 1000);

    expect(result.data).toHaveLength(0);
    expect(mockSearchService.search).toHaveBeenCalledWith('카페', 127.0, 37.5, 1000);
  });
});
