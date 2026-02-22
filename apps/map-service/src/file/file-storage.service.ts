import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as fs from 'fs';
import * as path from 'path';
import { randomUUID } from 'crypto';

@Injectable()
export class FileStorageService {
  private readonly uploadDir: string;

  constructor(private readonly config: ConfigService) {
    this.uploadDir = config.get('FILE_UPLOAD_DIR', 'uploads');
  }

  async store(file: Express.Multer.File): Promise<string> {
    await fs.promises.mkdir(this.uploadDir, { recursive: true });

    const ext = path.extname(file.originalname);
    const filename = `${randomUUID()}${ext}`;
    const filepath = path.join(this.uploadDir, filename);
    await fs.promises.writeFile(filepath, file.buffer);

    return filepath;
  }
}
