import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import axios from 'axios';
import { KakaoTokenResponse } from './kakao-token-response';
import { KakaoUserInfoResponse } from './kakao-user-info-response';

@Injectable()
export class KakaoApiClient {
  private readonly clientId: string;
  private readonly redirectUri: string;
  private readonly tokenUri: string;
  private readonly userInfoUri: string;

  constructor(private readonly config: ConfigService) {
    this.clientId = config.get<string>('KAKAO_CLIENT_ID', 'test');
    this.redirectUri = config.get<string>('KAKAO_REDIRECT_URI', 'http://localhost');
    this.tokenUri = config.get<string>(
      'KAKAO_TOKEN_URI',
      'https://kauth.kakao.com/oauth/token',
    );
    this.userInfoUri = config.get<string>(
      'KAKAO_USER_INFO_URI',
      'https://kapi.kakao.com/v2/user/me',
    );
  }

  async getAccessToken(authCode: string): Promise<KakaoTokenResponse> {
    const params = new URLSearchParams({
      grant_type: 'authorization_code',
      client_id: this.clientId,
      redirect_uri: this.redirectUri,
      code: authCode,
    });

    const response = await axios.post<KakaoTokenResponse>(
      this.tokenUri,
      params.toString(),
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } },
    );

    return response.data;
  }

  async getUserInfo(accessToken: string): Promise<KakaoUserInfoResponse> {
    const response = await axios.get<KakaoUserInfoResponse>(this.userInfoUri, {
      headers: { Authorization: `Bearer ${accessToken}` },
    });

    return response.data;
  }
}
