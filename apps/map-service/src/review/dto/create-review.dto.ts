import {
  IsNotEmpty,
  IsNumber,
  IsOptional,
  IsString,
  Max,
  MaxLength,
  Min,
} from 'class-validator';

export class CreateReviewDto {
  @IsNotEmpty({ message: '외부 식당 ID는 필수입니다' })
  @IsString()
  externalId: string;

  @IsNotEmpty({ message: '식당 이름은 필수입니다' })
  @IsString()
  restaurantName: string;

  @IsOptional()
  @IsString()
  address?: string;

  @IsOptional()
  @IsString()
  category?: string;

  @IsNumber()
  x: number;

  @IsNumber()
  y: number;

  @IsNotEmpty({ message: '리뷰 내용은 필수입니다' })
  @IsString()
  @MaxLength(2000)
  content: string;

  @IsNumber()
  @Min(1)
  @Max(5)
  rating: number;

  @IsNotEmpty({ message: '공개 범위는 필수입니다' })
  @IsString()
  visibility: string;

  @IsOptional()
  @IsNumber()
  groupId?: number;
}
