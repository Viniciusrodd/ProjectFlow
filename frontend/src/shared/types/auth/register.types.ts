
// uuid
export type UUID = string;


// register DTO
export interface RegisterDTO {
   name: string;
   email: string;
   password: string;
}


// register response DTO
export interface RegisterResponseDTO {
   id: UUID | null;   
   name: string;
   email: string;
   password: string;
   createdAt: string;  
}