
// api response
export interface iApiResponse<T = unknown>{
   success: boolean;
   statusCode: number;
   message: string; 
   data?: T;
};


// request config
export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
export interface iApiRequest {
   method: HttpMethod;
   path: string;
   body?: object;
};


// api requests
export interface iApi {

   request(req: iApiRequest): Promise<iApiResponse>

};