import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { In, Repository } from 'typeorm';
import { Review } from './review.entity';

@Injectable()
export class ReviewRepository {
  constructor(
    @InjectRepository(Review)
    private readonly repo: Repository<Review>,
  ) {}

  save(review: Review): Promise<Review> {
    return this.repo.save(review);
  }

  findOneWithRelations(id: number): Promise<Review | null> {
    return this.repo.findOne({
      where: { id },
      relations: ['restaurant', 'images'],
    });
  }

  countByRestaurantExternalId(externalId: string): Promise<number> {
    return this.repo
      .createQueryBuilder('review')
      .leftJoin('review.restaurant', 'restaurant')
      .where('restaurant.externalId = :externalId', { externalId })
      .getCount();
  }

  findByUserIdsAndGroupId(
    userIds: number[],
    groupId: number,
    skip: number,
    take: number,
  ): Promise<Review[]> {
    return this.repo.find({
      where: { userId: In(userIds), groupId },
      order: { createdAt: 'DESC' },
      relations: ['restaurant', 'images'],
      skip,
      take,
    });
  }
}
