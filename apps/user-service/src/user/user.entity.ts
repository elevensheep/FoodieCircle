import {
  Entity,
  Column,
  PrimaryGeneratedColumn,
  CreateDateColumn,
  UpdateDateColumn,
  BeforeInsert,
} from 'typeorm';
import { v4 as uuidv4 } from 'uuid';

@Entity('users')
export class User {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ name: 'kakao_id', type: 'bigint', nullable: false, unique: true })
  kakaoId: string;

  @Column({ nullable: false })
  nickname: string;

  @Column({ nullable: true })
  email: string;

  @Column({ name: 'profile_image_url', nullable: true })
  profileImageUrl: string;

  @Column({ type: 'uuid', nullable: false, unique: true, update: false })
  uuid: string;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt: Date;

  @BeforeInsert()
  generateUuid() {
    if (!this.uuid) {
      this.uuid = uuidv4();
    }
  }

  updateProfile(nickname: string, email: string, profileImageUrl: string): void {
    this.nickname = nickname;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
  }
}
