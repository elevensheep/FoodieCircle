import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { CircleMember } from './circle-member.entity';

@Injectable()
export class CircleMemberRepository {
  constructor(
    @InjectRepository(CircleMember)
    private readonly repo: Repository<CircleMember>,
  ) {}

  findByUserId(userId: number): Promise<CircleMember[]> {
    return this.repo.find({
      where: { userId },
      relations: ['circle', 'circle.members'],
    });
  }

  findByCircleId(circleId: number): Promise<CircleMember[]> {
    return this.repo.find({ where: { circle: { id: circleId } } });
  }

  save(member: CircleMember): Promise<CircleMember> {
    return this.repo.save(member);
  }
}
