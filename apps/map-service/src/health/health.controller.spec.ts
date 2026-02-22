import { Test, TestingModule } from '@nestjs/testing';
import { HealthController } from './health.controller';

describe('HealthController', () => {
  let controller: HealthController;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [HealthController],
    }).compile();

    controller = module.get<HealthController>(HealthController);
  });

  it('health_map-service_is_running', () => {
    const result = controller.health();
    expect(result.status).toBe(200);
    expect(result.message).toBe('map-service is running');
    expect(result.data).toBeNull();
  });
});
