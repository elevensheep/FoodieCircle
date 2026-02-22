import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  ManyToOne,
  JoinColumn,
} from 'typeorm';
import { Review } from './review.entity';

@Entity('review_images')
export class ReviewImage {
  @PrimaryGeneratedColumn()
  id: number;

  @ManyToOne(() => Review, (review) => review.images)
  @JoinColumn({ name: 'review_id' })
  review: Review;

  @Column({ name: 'image_path', length: 500 })
  imagePath: string;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;
}
