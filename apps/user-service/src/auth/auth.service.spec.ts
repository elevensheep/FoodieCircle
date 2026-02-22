import { Test, TestingModule } from '@nestjs/testing';
import { AuthService } from './auth.service';
import { KakaoApiClient } from './kakao/kakao-api.client';
import { UserRepository } from '../user/user.repository';
import { JwtTokenProvider } from '../jwt/jwt-token.provider';
import { User } from '../user/user.entity';
import { v4 as uuidv4 } from 'uuid';

describe('AuthService', () => {
  let service: AuthService;

  const mockKakaoApiClient = {
    getAccessToken: jest.fn(),
    getUserInfo: jest.fn(),
  };

  const mockUserRepository = {
    findByKakaoId: jest.fn(),
    save: jest.fn(),
  };

  const mockJwtTokenProvider = {
    generateAccessToken: jest.fn(),
    generateRefreshToken: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [
        AuthService,
        { provide: KakaoApiClient, useValue: mockKakaoApiClient },
        { provide: UserRepository, useValue: mockUserRepository },
        { provide: JwtTokenProvider, useValue: mockJwtTokenProvider },
      ],
    }).compile();

    service = module.get<AuthService>(AuthService);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  const mockTokenResponse = {
    access_token: 'kakao-access-token',
    token_type: 'bearer',
    refresh_token: 'kakao-refresh-token',
    expires_in: 7200,
    refresh_token_expires_in: 5184000,
  };

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

  it('kakaoLogin_신규회원_회원가입_및_JWT_발급', async () => {
    const savedUser = new User();
    savedUser.id = 1;
    savedUser.kakaoId = '12345';
    savedUser.nickname = '테스터';
    savedUser.email = 'test@kakao.com';
    savedUser.profileImageUrl = 'http://img.kakao.com/profile.jpg';
    savedUser.uuid = uuidv4();

    mockKakaoApiClient.getAccessToken.mockResolvedValue(mockTokenResponse);
    mockKakaoApiClient.getUserInfo.mockResolvedValue(mockUserInfo);
    mockUserRepository.findByKakaoId.mockResolvedValue(null);
    mockUserRepository.save.mockResolvedValue(savedUser);
    mockJwtTokenProvider.generateAccessToken.mockReturnValue('jwt-access-token');
    mockJwtTokenProvider.generateRefreshToken.mockReturnValue('jwt-refresh-token');

    const result = await service.kakaoLogin('test-auth-code');

    expect(result.accessToken).toBe('jwt-access-token');
    expect(result.refreshToken).toBe('jwt-refresh-token');
    expect(result.nickname).toBe('테스터');
    expect(result.isNewMember).toBe(true);
    expect(mockUserRepository.save).toHaveBeenCalled();
  });

  it('kakaoLogin_기존회원_정보업데이트_및_JWT_발급', async () => {
    const existingUser = new User();
    existingUser.id = 1;
    existingUser.kakaoId = '12345';
    existingUser.nickname = '원래닉네임';
    existingUser.email = 'old@kakao.com';
    existingUser.uuid = uuidv4();

    const updatedUserInfo = {
      id: 12345,
      kakao_account: {
        email: 'updated@kakao.com',
        profile: {
          nickname: '새닉네임',
          profile_image_url: 'http://img.kakao.com/new.jpg',
        },
      },
    };

    const updatedUser = { ...existingUser, nickname: '새닉네임', email: 'updated@kakao.com' };

    mockKakaoApiClient.getAccessToken.mockResolvedValue(mockTokenResponse);
    mockKakaoApiClient.getUserInfo.mockResolvedValue(updatedUserInfo);
    mockUserRepository.findByKakaoId.mockResolvedValue(existingUser);
    mockUserRepository.save.mockResolvedValue(updatedUser);
    mockJwtTokenProvider.generateAccessToken.mockReturnValue('jwt-access-token');
    mockJwtTokenProvider.generateRefreshToken.mockReturnValue('jwt-refresh-token');

    const result = await service.kakaoLogin('test-auth-code');

    expect(result.isNewMember).toBe(false);
    expect(result.accessToken).toBe('jwt-access-token');
    expect(mockUserRepository.save).toHaveBeenCalled();
  });

  it('kakaoLogin_카카오API_실패시_예외_전파', async () => {
    mockKakaoApiClient.getAccessToken.mockRejectedValue(new Error('카카오 API 호출 실패'));

    await expect(service.kakaoLogin('bad-code')).rejects.toThrow('카카오 API 호출 실패');
  });
});
