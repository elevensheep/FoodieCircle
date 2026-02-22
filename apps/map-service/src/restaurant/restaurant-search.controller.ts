import { Controller, Get, Query } from '@nestjs/common';
import { ApiResponse } from '@app/common';
import { RestaurantSearchService } from './restaurant-search.service';
import { RestaurantSearchResponseDto } from './dto/restaurant-search-response.dto';

@Controller('api/map/restaurants')
export class RestaurantSearchController {
  constructor(private readonly restaurantSearchService: RestaurantSearchService) {}

  @Get('search')
  async search(
    @Query('keyword') keyword: string,
    @Query('x') x: number,
    @Query('y') y: number,
    @Query('radius') radius: number = 1000,
  ): Promise<ApiResponse<RestaurantSearchResponseDto[]>> {
    const results = await this.restaurantSearchService.search(keyword, x, y, radius);
    return ApiResponse.success('식당 검색 성공', results);
  }
}
