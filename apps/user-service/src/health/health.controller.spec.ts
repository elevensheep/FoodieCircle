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

  it('health_returnsOkStatus', () => {
    const result = controller.health();

    expect(result.status).toBe(200);
    expect(result.message).toBe('user-service is running');
    expect(result.data).toBeNull();
  });
});
