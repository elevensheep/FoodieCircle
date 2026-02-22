import { Test, TestingModule } from '@nestjs/testing';
import { KakaoApiClient } from './kakao-api.client';
import { ConfigService } from '@nestjs/config';
import axios from 'axios';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('KakaoApiClient', () => {
  let client: KakaoApiClient;

  const mockConfigService = {
    get: jest.fn((key: string, defaultValue?: any) => {
      const config: Record<string, string> = {
        KAKAO_CLIENT_ID: 'test-client-id',
        KAKAO_REDIRECT_URI: 'http://localhost/callback',
        KAKAO_TOKEN_URI: 'https://kauth.kakao.com/oauth/token',
        KAKAO_USER_INFO_URI: 'https://kapi.kakao.com/v2/user/me',
      };
      return config[key] ?? defaultValue;
    }),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        KakaoApiClient,
        { provide: ConfigService, useValue: mockConfigService },
      ],
    }).compile();

    client = module.get<KakaoApiClient>(KakaoApiClient);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('getAccessToken_인가코드로_토큰_교환_성공', async () => {
    const mockTokenResponse = {
      access_token: 'mock-access-token',
      token_type: 'bearer',
      refresh_token: 'mock-refresh-token',
      expires_in: 7200,
      refresh_token_expires_in: 5184000,
    };

    mockedAxios.post = jest.fn().mockResolvedValue({ data: mockTokenResponse });

    const result = await client.getAccessToken('test-auth-code');

    expect(result.access_token).toBe('mock-access-token');
    expect(result.refresh_token).toBe('mock-refresh-token');
    expect(mockedAxios.post).toHaveBeenCalledWith(
      'https://kauth.kakao.com/oauth/token',
      expect.any(String),
      expect.objectContaining({ headers: expect.any(Object) }),
    );
  });

  it('getUserInfo_액세스토큰으로_사용자정보_조회_성공', async () => {
    const mockUserInfo = {
      id: 12345,
      kakao_account: {
        email: 'test@kakao.com',
        profile: {
          nickname: '테스터',
          profile_image_url: 'http://img.kakao.com/profile.jpg',
        },
      },
    };

    mockedAxios.get = jest.fn().mockResolvedValue({ data: mockUserInfo });

    const result = await client.getUserInfo('mock-access-token');

    expect(result.id).toBe(12345);
    expect(result.kakao_account?.profile?.nickname).toBe('테스터');
    expect(result.kakao_account?.email).toBe('test@kakao.com');
    expect(mockedAxios.get).toHaveBeenCalledWith(
      'https://kapi.kakao.com/v2/user/me',
      expect.objectContaining({
        headers: { Authorization: 'Bearer mock-access-token' },
      }),
    );
  });

  it('getAccessToken_카카오API_실패시_예외_발생', async () => {
    mockedAxios.post = jest.fn().mockRejectedValue(new Error('Server Error'));

    await expect(client.getAccessToken('invalid-code')).rejects.toThrow('Server Error');
  });
});
