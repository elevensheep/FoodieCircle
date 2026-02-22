import { Module, forwardRef } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Restaurant } from './restaurant.entity';
import { RestaurantRepository } from './restaurant.repository';
import { KakaoLocalApiClient } from './kakao-local-api.client';
import { RestaurantSearchService } from './restaurant-search.service';
import { RestaurantSearchController } from './restaurant-search.controller';
import { ReviewModule } from '../review/review.module';

@Module({
  imports: [TypeOrmModule.forFeature([Restaurant]), forwardRef(() => ReviewModule)],
  providers: [RestaurantRepository, KakaoLocalApiClient, RestaurantSearchService],
  controllers: [RestaurantSearchController],
  exports: [RestaurantRepository],
})
export class RestaurantModule {}
