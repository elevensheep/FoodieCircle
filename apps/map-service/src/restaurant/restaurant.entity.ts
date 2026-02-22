import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
} from 'typeorm';

@Entity('restaurants')
export class Restaurant {
  @PrimaryGeneratedColumn()
  id: number;

  @Column({ name: 'external_id', unique: true })
  externalId: string;

  @Column({ length: 200 })
  name: string;

  @Column({ length: 500, nullable: true })
  address: string;

  @Column({ length: 100, nullable: true })
  category: string;

  @Column({ type: 'double precision' })
  x: number;

  @Column({ type: 'double precision' })
  y: number;

  @CreateDateColumn({ name: 'created_at' })
  createdAt: Date;
}
