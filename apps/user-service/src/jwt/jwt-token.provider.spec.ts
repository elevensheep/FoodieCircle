import { Test, TestingModule } from '@nestjs/testing';
import { JwtTokenProvider } from './jwt-token.provider';
import { ConfigService } from '@nestjs/config';

const SECRET =
  'dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi10aGlzLW11c3QtYmUtbG9uZy1lbm91Z2g=';

describe('JwtTokenProvider', () => {
  let provider: JwtTokenProvider;

  const mockConfigService = {
    get: jest.fn((key: string, defaultValue?: any) => {
      const config: Record<string, any> = {
        JWT_SECRET: SECRET,
        JWT_ACCESS_TOKEN_EXPIRY: 3600000,
        JWT_REFRESH_TOKEN_EXPIRY: 604800000,
      };
      return config[key] ?? defaultValue;
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        JwtTokenProvider,
        { provide: ConfigService, useValue: mockConfigService },
      ],
    }).compile();

    provider = module.get<JwtTokenProvider>(JwtTokenProvider);
  });

  it('generateAccessToken_유효한_토큰_생성', () => {
    const uuid = 'test-uuid-1234';

    const token = provider.generateAccessToken(uuid);

    expect(token).toBeTruthy();
    expect(provider.validateToken(token)).toBe(true);
  });

  it('generateRefreshToken_유효한_토큰_생성', () => {
    const uuid = 'test-uuid-5678';

    const token = provider.generateRefreshToken(uuid);

    expect(token).toBeTruthy();
    expect(provider.validateToken(token)).toBe(true);
  });

  it('getUuidFromToken_토큰에서_UUID_추출', () => {
    const uuid = 'test-uuid-extraction';
    const token = provider.generateAccessToken(uuid);

    const extracted = provider.getUuidFromToken(token);

    expect(extracted).toBe(uuid);
  });

  it('validateToken_만료된_토큰_검증_실패', () => {
    const shortLivedMockConfig = {
      get: jest.fn((key: string, defaultValue?: any) => {
        const config: Record<string, any> = {
          JWT_SECRET: SECRET,
          JWT_ACCESS_TOKEN_EXPIRY: -1000,
          JWT_REFRESH_TOKEN_EXPIRY: -1000,
        };
        return config[key] ?? defaultValue;
      }),
    };

    const shortLivedProvider = new JwtTokenProvider(shortLivedMockConfig as any);
    const token = shortLivedProvider.generateAccessToken('some-uuid');

    expect(provider.validateToken(token)).toBe(false);
  });

  it('validateToken_잘못된_토큰_검증_실패', () => {
    expect(provider.validateToken('invalid.token.here')).toBe(false);
  });
});
