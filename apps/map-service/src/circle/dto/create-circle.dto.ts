import { IsNotEmpty, IsString, MaxLength } from 'class-validator';

export class CreateCircleDto {
  @IsNotEmpty({ message: '그룹 이름은 필수입니다' })
  @IsString()
  @MaxLength(100, { message: '그룹 이름은 100자 이하여야 합니다' })
  name: string;
}
