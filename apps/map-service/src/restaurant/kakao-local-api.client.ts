import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import axios from 'axios';
import { KakaoPlaceDto } from './dto/kakao-place.dto';

@Injectable()
export class KakaoLocalApiClient {
  private readonly apiUrl = 'https://dapi.kakao.com/v2/local/search/keyword.json';
  private readonly apiKey: string;

  constructor(private readonly config: ConfigService) {
    this.apiKey = config.get('KAKAO_API_KEY', '');
  }

  async searchByKeyword(
    keyword: string,
    x: number,
    y: number,
    radius: number,
  ): Promise<KakaoPlaceDto[]> {
    try {
      const response = await axios.get(this.apiUrl, {
        headers: { Authorization: `KakaoAK ${this.apiKey}` },
        params: { query: keyword, x, y, radius, category_group_code: 'FD6' },
      });

      const documents: any[] = response.data?.documents ?? [];
      return documents.map((doc) => ({
        id: doc.id,
        placeName: doc.place_name,
        addressName: doc.address_name,
        categoryName: doc.category_name,
        x: doc.x,
        y: doc.y,
      }));
    } catch {
      return [];
    }
  }
}
