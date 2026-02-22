import { Injectable, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Kafka, Producer } from 'kafkajs';
import { MarkerCreatedEvent } from './marker-created.event';

@Injectable()
export class MarkerEventPublisher implements OnModuleInit, OnModuleDestroy {
  private readonly producer: Producer;

  constructor(private readonly config: ConfigService) {
    const kafka = new Kafka({
      brokers: [config.get('KAFKA_BROKERS', 'localhost:9092')],
    });
    this.producer = kafka.producer();
  }

  async onModuleInit(): Promise<void> {
    await this.producer.connect();
  }

  async onModuleDestroy(): Promise<void> {
    await this.producer.disconnect();
  }

  async publish(event: MarkerCreatedEvent): Promise<void> {
    await this.producer.send({
      topic: 'marker-created',
      messages: [{ value: JSON.stringify(event) }],
    });
  }
}
