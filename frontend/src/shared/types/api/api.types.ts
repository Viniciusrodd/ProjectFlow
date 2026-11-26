
// request config
export type HttpMethod = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
export interface iApiRequest {
   method: HttpMethod;
   path: string;
   body?: object;
   params?: object;
   private?: boolean;
};


// api response file
export interface iApiFile {
   bytes: Uint8Array;
   mimeType: string;
   fileName?: string;
}


// api response
export interface iApiResponse<T = unknown>{
   success: boolean;
   statusCode: number;
   message: string; 
   data?: T;
};


// api requests
export interface iApi {

   request<T = unknown>(req: iApiRequest): Promise<iApiResponse<T>>
   requestFile(req: iApiRequest): Promise<iApiResponse<iApiFile>>

};