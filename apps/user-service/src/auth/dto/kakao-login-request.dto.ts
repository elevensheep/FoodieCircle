import { IsNotEmpty, IsString } from 'class-validator';

export class KakaoLoginRequestDto {
  @IsNotEmpty({ message: '인가 코드는 필수입니다' })
  @IsString()
  authCode: string;
}
