export interface KakaoUserInfoResponse {
  id: number;
  kakao_account?: {
    email?: string;
    profile?: {
      nickname?: string;
      profile_image_url?: string;
    };
  };
}

export function getNickname(info: KakaoUserInfoResponse): string | null {
  return info.kakao_account?.profile?.nickname ?? null;
}

export function getEmail(info: KakaoUserInfoResponse): string | null {
  return info.kakao_account?.email ?? null;
}

export function getProfileImageUrl(info: KakaoUserInfoResponse): string | null {
  return info.kakao_account?.profile?.profile_image_url ?? null;
}
