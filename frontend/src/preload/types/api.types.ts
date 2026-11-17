
// api response
export interface iApiResponse<T = unknown>{
   message: string; 
   statusCode: number;
   success: boolean 
   data?: T;
};


// request config
export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
export interface iConfig {
   method: HttpMethod;
   path: string;
   body?: object;
};


// api requests
export interface iApi {

   request(config: iConfig): Promise<iApiResponse>

};