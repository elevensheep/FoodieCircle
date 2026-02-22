import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Circle } from './circle.entity';

@Injectable()
export class CircleRepository {
  constructor(
    @InjectRepository(Circle)
    private readonly repo: Repository<Circle>,
  ) {}

  findByOwnerId(ownerId: number): Promise<Circle[]> {
    return this.repo.find({
      where: { ownerId },
      relations: ['members'],
    });
  }

  save(circle: Circle): Promise<Circle> {
    return this.repo.save(circle);
  }
}
