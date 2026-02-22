import { Test, TestingModule } from '@nestjs/testing';
import { CircleController } from './circle.controller';
import { CircleService } from './circle.service';
import { CircleResponseDto } from './dto/circle-response.dto';
import { CreateCircleDto } from './dto/create-circle.dto';

describe('CircleController', () => {
  let controller: CircleController;

  const mockCircleService = {
    createCircle: jest.fn(),
    getMyCircles: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [CircleController],
      providers: [{ provide: CircleService, useValue: mockCircleService }],
    }).compile();

    controller = module.get<CircleController>(CircleController);
  });

  afterEach(() => jest.clearAllMocks());

  it('createCircle_그룹_생성_성공', async () => {
    const dto: CreateCircleDto = { name: '직장 점심' };
    const response: CircleResponseDto = {
      id: 1,
      name: '직장 점심',
      ownerId: 1,
      memberCount: 1,
      createdAt: new Date(),
    };
    mockCircleService.createCircle.mockResolvedValue(response);

    const result = await controller.createCircle(dto, '1');

    expect(result.status).toBe(200);
    expect(result.message).toBe('그룹 생성 성공');
    expect(result.data.name).toBe('직장 점심');
    expect(mockCircleService.createCircle).toHaveBeenCalledWith(dto, 1);
  });

  it('getMyCircles_내_그룹_목록_조회_성공', async () => {
    const response: CircleResponseDto[] = [
      { id: 1, name: '모임', ownerId: 1, memberCount: 3, createdAt: new Date() },
    ];
    mockCircleService.getMyCircles.mockResolvedValue(response);

    const result = await controller.getMyCircles('1');

    expect(result.status).toBe(200);
    expect(result.message).toBe('내 그룹 목록 조회 성공');
    expect(result.data).toHaveLength(1);
    expect(result.data[0].name).toBe('모임');
    expect(mockCircleService.getMyCircles).toHaveBeenCalledWith(1);
  });
});
