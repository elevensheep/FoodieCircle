import { Module } from '@nestjs/common';
import { JwtTokenProvider } from './jwt-token.provider';

@Module({
  providers: [JwtTokenProvider],
  exports: [JwtTokenProvider],
})
export class JwtModule {}
