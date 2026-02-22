import { Controller, Get } from '@nestjs/common';
import { ApiResponse } from '@app/common';

@Controller('api/map')
export class HealthController {
  @Get('health')
  health(): ApiResponse<void> {
    return ApiResponse.success('map-service is running');
  }
}
