import { Body, Controller, Get, Headers, Post } from '@nestjs/common';
import { ApiResponse } from '@app/common';
import { CircleService } from './circle.service';
import { CreateCircleDto } from './dto/create-circle.dto';
import { CircleResponseDto } from './dto/circle-response.dto';

@Controller('api/map/groups')
export class CircleController {
  constructor(private readonly circleService: CircleService) {}

  @Post()
  async createCircle(
    @Body() dto: CreateCircleDto,
    @Headers('x-user-id') userId: string,
  ): Promise<ApiResponse<CircleResponseDto>> {
    const response = await this.circleService.createCircle(dto, parseInt(userId));
    return ApiResponse.success('그룹 생성 성공', response);
  }

  @Get()
  async getMyCircles(
    @Headers('x-user-id') userId: string,
  ): Promise<ApiResponse<CircleResponseDto[]>> {
    const circles = await this.circleService.getMyCircles(parseInt(userId));
    return ApiResponse.success('내 그룹 목록 조회 성공', circles);
  }
}
