import {
  Body,
  Controller,
  Get,
  Headers,
  Post,
  Query,
  UploadedFiles,
  UseInterceptors,
} from '@nestjs/common';
import { FileFieldsInterceptor } from '@nestjs/platform-express';
import { ApiResponse } from '@app/common';
import { ReviewService } from './review.service';
import { CreateReviewDto } from './dto/create-review.dto';
import { ReviewResponseDto } from './dto/review-response.dto';

@Controller('api/map/reviews')
export class ReviewController {
  constructor(private readonly reviewService: ReviewService) {}

  @Post()
  @UseInterceptors(FileFieldsInterceptor([{ name: 'images', maxCount: 10 }]))
  async createReview(
    @Body() dto: CreateReviewDto,
    @UploadedFiles() files: { images?: Express.Multer.File[] },
    @Headers('x-user-id') userId: string,
  ): Promise<ApiResponse<ReviewResponseDto>> {
    const response = await this.reviewService.createReview(dto, parseInt(userId), files?.images);
    return ApiResponse.success('리뷰 등록 성공', response);
  }

  @Get('feed')
  async getFeed(
    @Query('groupId') groupId: number,
    @Query('page') page: number = 0,
    @Query('size') size: number = 10,
  ): Promise<ApiResponse<ReviewResponseDto[]>> {
    const feed = await this.reviewService.getFeed(groupId, page, size);
    return ApiResponse.success('피드 조회 성공', feed);
  }
}
