import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { Restaurant } from './restaurant.entity';

@Injectable()
export class RestaurantRepository {
  constructor(
    @InjectRepository(Restaurant)
    private readonly repo: Repository<Restaurant>,
  ) {}

  findByExternalId(externalId: string): Promise<Restaurant | null> {
    return this.repo.findOne({ where: { externalId } });
  }

  save(restaurant: Restaurant): Promise<Restaurant> {
    return this.repo.save(restaurant);
  }

}
