import { Controller, Get } from '@nestjs/common';
import { ApiResponse } from '@app/common';

@Controller('api/user')
export class HealthController {
  @Get('health')
  health(): ApiResponse<void> {
    return ApiResponse.success('user-service is running');
  }
}
