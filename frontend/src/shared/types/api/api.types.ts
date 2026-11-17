
// api response
export interface iApiResponse<T = unknown>{
   success: boolean;
   statusCode: number;
   message: string; 
   data?: T;
};