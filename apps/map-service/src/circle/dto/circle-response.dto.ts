import { Circle } from '../circle.entity';

export class CircleResponseDto {
  id: number;
  name: string;
  ownerId: number;
  memberCount: number;
  createdAt: Date;

  static from(circle: Circle): CircleResponseDto {
    const dto = new CircleResponseDto();
    dto.id = circle.id;
    dto.name = circle.name;
    dto.ownerId = circle.ownerId;
    dto.memberCount = circle.members?.length ?? 0;
    dto.createdAt = circle.createdAt;
    return dto;
  }
}
