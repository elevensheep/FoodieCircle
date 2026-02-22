import { Module, forwardRef } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Review } from './review.entity';
import { ReviewImage } from './review-image.entity';
import { ReviewRepository } from './review.repository';
import { ReviewService } from './review.service';
import { ReviewController } from './review.controller';
import { FileStorageService } from '../file/file-storage.service';
import { MarkerEventPublisher } from '../event/marker-event.publisher';
import { CircleModule } from '../circle/circle.module';
import { RestaurantModule } from '../restaurant/restaurant.module';

@Module({
  imports: [
    TypeOrmModule.forFeature([Review, ReviewImage]),
    forwardRef(() => RestaurantModule),
    CircleModule,
  ],
  providers: [ReviewRepository, ReviewService, FileStorageService, MarkerEventPublisher],
  controllers: [ReviewController],
  exports: [ReviewRepository],
})
export class ReviewModule {}
