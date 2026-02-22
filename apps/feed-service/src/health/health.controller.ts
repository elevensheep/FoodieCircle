import { Controller, Get } from '@nestjs/common';

@Controller('api/feed')
export class HealthController {
    @Get('health')
    health(): any {
        return {
            status: 'success',
            message: 'feed-service is running',
            data: null,
        };
    }
}
