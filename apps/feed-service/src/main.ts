import { NestFactory } from '@nestjs/core';
import { FeedServiceModule } from './feed-service.module';

async function bootstrap() {
  const app = await NestFactory.create(FeedServiceModule);
  await app.listen(process.env.port ?? 3000);
}
bootstrap();
