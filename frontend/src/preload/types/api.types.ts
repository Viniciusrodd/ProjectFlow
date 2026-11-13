
// api response
export interface iApiResponse<T = unknown>{
   message: string; 
   statusCode: number;
   success: boolean 
   data?: T;
};


// request config
export interface iConfig {
   method: string;
   path: string;
   body?: object;
};


// api requests
export interface iApi {

   request(config: iConfig): Promise<iApiResponse>

};