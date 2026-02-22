import { Controller, Post, Body } from '@nestjs/common';
import { AuthService } from './auth.service';
import { KakaoLoginRequestDto } from './dto/kakao-login-request.dto';
import { KakaoLoginResponseDto } from './dto/kakao-login-response.dto';
import { ApiResponse } from '@app/common';

@Controller('api/user/auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @Post('login/kakao')
  async kakaoLogin(
    @Body() request: KakaoLoginRequestDto,
  ): Promise<ApiResponse<KakaoLoginResponseDto>> {
    const response = await this.authService.kakaoLogin(request.authCode);
    return ApiResponse.success('로그인 성공', response);
  }
}
