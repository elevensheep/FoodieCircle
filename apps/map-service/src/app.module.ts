import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Circle } from './circle/circle.entity';
import { CircleMember } from './circle/circle-member.entity';
import { Restaurant } from './restaurant/restaurant.entity';
import { Review } from './review/review.entity';
import { ReviewImage } from './review/review-image.entity';
import { HealthModule } from './health/health.module';
import { CircleModule } from './circle/circle.module';
import { RestaurantModule } from './restaurant/restaurant.module';
import { ReviewModule } from './review/review.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (config: ConfigService) => ({
        type: 'postgres',
        host: config.get('DB_HOST', 'localhost'),
        port: 5432,
        username: config.get('DB_USERNAME', 'user'),
        password: config.get('DB_PASSWORD', 'password'),
        database: config.get('DB_NAME', 'foodicircle'),
        schema: 'map_schema',
        entities: [Circle, CircleMember, Restaurant, Review, ReviewImage],
        synchronize: true,
      }),
      inject: [ConfigService],
    }),
    HealthModule,
    CircleModule,
    RestaurantModule,
    ReviewModule,
  ],
})
export class AppModule {}
