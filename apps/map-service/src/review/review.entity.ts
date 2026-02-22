import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
  OneToMany,
  JoinColumn,
} from 'typeorm';
import { Restaurant } from '../restaurant/restaurant.entity';
import { ReviewImage } from './review-image.entity';

export enum ReviewVisibility {
  PUBLIC = 'PUBLIC',
  GROUP = 'GROUP',
}

@Entity('reviews')
export class Review {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => Restaurant)
  @JoinColumn({ name: 'restaurant_id' })
  restaurant: Restaurant;

  @Column({ name: 'user_id' })
  userId: number;

  @Column({ length: 2000 })
  content: string;

  @Column()
  rating: number;

  @Column({ type: 'varchar', nullable: false })
  visibility: ReviewVisibility;

  @Column({ name: 'group_id', nullable: true })
  groupId: number;

  @OneToMany(() => ReviewImage, (image) => image.review, { cascade: true })
  images: ReviewImage[];

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;
}
