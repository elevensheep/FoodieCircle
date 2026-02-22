import { Injectable } from '@nestjs/common';
import { CircleRepository } from './circle.repository';
import { CircleMemberRepository } from './circle-member.repository';
import { CreateCircleDto } from './dto/create-circle.dto';
import { CircleResponseDto } from './dto/circle-response.dto';
import { Circle } from './circle.entity';
import { CircleMember } from './circle-member.entity';

@Injectable()
export class CircleService {
  constructor(
    private readonly circleRepository: CircleRepository,
    private readonly circleMemberRepository: CircleMemberRepository,
  ) {}

  async createCircle(dto: CreateCircleDto, userId: number): Promise<CircleResponseDto> {
    const circle = new Circle();
    circle.name = dto.name;
    circle.ownerId = userId;
    circle.members = [];

    const saved = await this.circleRepository.save(circle);

    const ownerMember = new CircleMember();
    ownerMember.circle = saved;
    ownerMember.userId = userId;
    await this.circleMemberRepository.save(ownerMember);

    saved.members = [ownerMember];
    return CircleResponseDto.from(saved);
  }

  async getMyCircles(userId: number): Promise<CircleResponseDto[]> {
    const ownedCircles = await this.circleRepository.findByOwnerId(userId);
    const memberships = await this.circleMemberRepository.findByUserId(userId);
    const memberCircles = memberships.map((m) => m.circle);

    const uniqueCircles = new Map<number, Circle>();
    [...ownedCircles, ...memberCircles].forEach((c) => uniqueCircles.set(c.id, c));

    return Array.from(uniqueCircles.values()).map(CircleResponseDto.from);
  }
}
