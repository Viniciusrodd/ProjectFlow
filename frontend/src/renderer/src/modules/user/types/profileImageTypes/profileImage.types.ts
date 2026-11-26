
// import types
import { iApiResponse, iApiFile } from "@shared/types/api/api.types";


// uuid
export type UUID = string;


// profile image response DTO
export interface ProfileImageResponseDTO {
   id: UUID | null;
   userId: UUID | null;
   fileName: string;
   mimeType: string;
   size: number;
   uploadDate: string;
}


// profile image methods
export interface iProfileImageMethods {

   uploadProfileImage(userId: UUID, file: File): Promise<iApiResponse<ProfileImageResponseDTO>>
   getProfileImage(userId: UUID): Promise<iApiResponse<ProfileImageResponseDTO>>
   getProfileImageData(userId: UUID): Promise<iApiResponse<iApiFile>>
   deleteProfileImage(userId: UUID): Promise<iApiResponse<void>>

}