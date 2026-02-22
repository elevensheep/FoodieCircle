import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as jwt from 'jsonwebtoken';

@Injectable()
export class JwtTokenProvider {
  private readonly secret: string;
  private readonly accessTokenExpiry: number;
  private readonly refreshTokenExpiry: number;

  constructor(private readonly config: ConfigService) {
    this.secret = config.get<string>(
      'JWT_SECRET',
      'dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi10aGlzLW11c3QtYmUtbG9uZy1lbm91Z2g=',
    );
    this.accessTokenExpiry = config.get<number>('JWT_ACCESS_TOKEN_EXPIRY', 3600000);
    this.refreshTokenExpiry = config.get<number>('JWT_REFRESH_TOKEN_EXPIRY', 604800000);
  }

  generateAccessToken(uuid: string): string {
    return this.generateToken(uuid, this.accessTokenExpiry);
  }

  generateRefreshToken(uuid: string): string {
    return this.generateToken(uuid, this.refreshTokenExpiry);
  }

  getUuidFromToken(token: string): string {
    const payload = jwt.verify(token, Buffer.from(this.secret, 'base64')) as jwt.JwtPayload;
    return payload.sub as string;
  }

  validateToken(token: string): boolean {
    try {
      jwt.verify(token, Buffer.from(this.secret, 'base64'));
      return true;
    } catch {
      return false;
    }
  }

  private generateToken(uuid: string, expiryMs: number): string {
    return jwt.sign(
      { sub: uuid },
      Buffer.from(this.secret, 'base64'),
      { expiresIn: Math.floor(expiryMs / 1000) },
    );
  }
}
