import { Injectable } from '@nestjs/common';
import { KakaoApiClient } from './kakao/kakao-api.client';
import { UserRepository } from '../user/user.repository';
import { JwtTokenProvider } from '../jwt/jwt-token.provider';
import { KakaoLoginResponseDto } from './dto/kakao-login-response.dto';
import { User } from '../user/user.entity';
import {
  getNickname,
  getEmail,
  getProfileImageUrl,
} from './kakao/kakao-user-info-response';

@Injectable()
export class AuthService {
  constructor(
    private readonly kakaoApiClient: KakaoApiClient,
    private readonly userRepository: UserRepository,
    private readonly jwtTokenProvider: JwtTokenProvider,
  ) {}

  async kakaoLogin(authCode: string): Promise<KakaoLoginResponseDto> {
    const kakaoToken = await this.kakaoApiClient.getAccessToken(authCode);
    const userInfo = await this.kakaoApiClient.getUserInfo(kakaoToken.access_token);

    let isNewMember = false;
    let user = await this.userRepository.findByKakaoId(String(userInfo.id));

    if (!user) {
      isNewMember = true;
      const newUser = new User();
      newUser.kakaoId = String(userInfo.id);
      newUser.nickname = getNickname(userInfo);
      newUser.email = getEmail(userInfo);
      newUser.profileImageUrl = getProfileImageUrl(userInfo);
      user = await this.userRepository.save(newUser);
    } else {
      user.updateProfile(
        getNickname(userInfo),
        getEmail(userInfo),
        getProfileImageUrl(userInfo),
      );
      user = await this.userRepository.save(user);
    }

    const accessToken = this.jwtTokenProvider.generateAccessToken(user.uuid);
    const refreshToken = this.jwtTokenProvider.generateRefreshToken(user.uuid);

    return {
      accessToken,
      refreshToken,
      userId: user.id,
      nickname: user.nickname,
      isNewMember,
    };
  }
}
