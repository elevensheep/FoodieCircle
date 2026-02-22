export class ApiResponse<T> {
  status: number;
  message: string;
  data: T | null;

  constructor(status: number, message: string, data: T | null = null) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  static success<T>(message: string, data?: T): ApiResponse<T> {
    return new ApiResponse<T>(200, message, data ?? null);
  }
}
