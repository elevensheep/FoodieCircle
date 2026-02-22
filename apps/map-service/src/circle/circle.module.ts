import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Circle } from './circle.entity';
import { CircleMember } from './circle-member.entity';
import { CircleRepository } from './circle.repository';
import { CircleMemberRepository } from './circle-member.repository';
import { CircleService } from './circle.service';
import { CircleController } from './circle.controller';

@Module({
  imports: [TypeOrmModule.forFeature([Circle, CircleMember])],
  providers: [CircleRepository, CircleMemberRepository, CircleService],
  controllers: [CircleController],
  exports: [CircleMemberRepository],
})
export class CircleModule {}
