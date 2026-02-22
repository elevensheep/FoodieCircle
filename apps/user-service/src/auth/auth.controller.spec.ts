import { Test, TestingModule } from '@nestjs/testing';
import { AuthController } from './auth.controller';
import { AuthService } from './auth.service';
import { KakaoLoginResponseDto } from './dto/kakao-login-response.dto';

describe('AuthController', () => {
  let controller: AuthController;

  const mockAuthService = {
    kakaoLogin: jest.fn(),
  };

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [AuthController],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
      ],
    }).compile();

    controller = module.get<AuthController>(AuthController);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('kakaoLogin_성공', async () => {
    const loginResponse: KakaoLoginResponseDto = {
      accessToken: 'jwt-access-token',
      refreshToken: 'jwt-refresh-token',
      userId: 1,
      nickname: '테스터',
      isNewMember: true,
    };

    mockAuthService.kakaoLogin.mockResolvedValue(loginResponse);

    const result = await controller.kakaoLogin({ authCode: 'test-auth-code' });

    expect(result.status).toBe(200);
    expect(result.message).toBe('로그인 성공');
    expect(result.data.accessToken).toBe('jwt-access-token');
    expect(result.data.refreshToken).toBe('jwt-refresh-token');
    expect(result.data.userId).toBe(1);
    expect(result.data.nickname).toBe('테스터');
    expect(result.data.isNewMember).toBe(true);
    expect(mockAuthService.kakaoLogin).toHaveBeenCalledWith('test-auth-code');
  });
});
