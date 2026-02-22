import { Test, TestingModule } from '@nestjs/testing';
import { CircleService } from './circle.service';
import { CircleRepository } from './circle.repository';
import { CircleMemberRepository } from './circle-member.repository';
import { Circle } from './circle.entity';
import { CircleMember } from './circle-member.entity';

describe('CircleService', () => {
  let service: CircleService;

  const mockCircleRepository = {
    save: jest.fn(),
    findByOwnerId: jest.fn(),
  };

  const mockCircleMemberRepository = {
    save: jest.fn(),
    findByUserId: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        CircleService,
        { provide: CircleRepository, useValue: mockCircleRepository },
        { provide: CircleMemberRepository, useValue: mockCircleMemberRepository },
      ],
    }).compile();

    service = module.get<CircleService>(CircleService);
  });

  afterEach(() => jest.clearAllMocks());

  it('createCircle_그룹_생성_및_오너_멤버_추가', async () => {
    const savedCircle = Object.assign(new Circle(), {
      id: 1,
      name: '직장 점심',
      ownerId: 1,
      members: [],
      createdAt: new Date(),
    });
    const savedMember = Object.assign(new CircleMember(), {
      id: 1,
      circle: savedCircle,
      userId: 1,
    });

    mockCircleRepository.save.mockResolvedValue(savedCircle);
    mockCircleMemberRepository.save.mockResolvedValue(savedMember);

    const result = await service.createCircle({ name: '직장 점심' }, 1);

    expect(result.name).toBe('직장 점심');
    expect(result.ownerId).toBe(1);
    expect(result.memberCount).toBe(1);
    expect(mockCircleRepository.save).toHaveBeenCalled();
    expect(mockCircleMemberRepository.save).toHaveBeenCalled();
  });

  it('getMyCircles_소유_및_참여_그룹_중복_제거_반환', async () => {
    const circle = Object.assign(new Circle(), {
      id: 1,
      name: '모임',
      ownerId: 1,
      members: [{}],
      createdAt: new Date(),
    });
    const membership = Object.assign(new CircleMember(), { circle });

    mockCircleRepository.findByOwnerId.mockResolvedValue([circle]);
    mockCircleMemberRepository.findByUserId.mockResolvedValue([membership]);

    const result = await service.getMyCircles(1);

    expect(result).toHaveLength(1);
    expect(result[0].id).toBe(1);
    expect(result[0].name).toBe('모임');
  });

  it('getMyCircles_소유_그룹과_멤버_그룹이_다를때_합산', async () => {
    const ownedCircle = Object.assign(new Circle(), {
      id: 1, name: '내 그룹', ownerId: 1, members: [], createdAt: new Date(),
    });
    const memberCircle = Object.assign(new Circle(), {
      id: 2, name: '친구 그룹', ownerId: 2, members: [{}], createdAt: new Date(),
    });
    const membership = Object.assign(new CircleMember(), { circle: memberCircle });

    mockCircleRepository.findByOwnerId.mockResolvedValue([ownedCircle]);
    mockCircleMemberRepository.findByUserId.mockResolvedValue([membership]);

    const result = await service.getMyCircles(1);

    expect(result).toHaveLength(2);
  });
});
