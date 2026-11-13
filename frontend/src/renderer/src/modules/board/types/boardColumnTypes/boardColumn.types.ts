
// import enums
import { BoardEnum } from '@board/enums/board.enum';

// uuid
export type UUID = string;


// boardColumns DTO
export interface BoardColumnsDTO {
   id: UUID | null;
   name: string;
   position: number;
   color: string;
}


// boardColumns deleted DTO
export interface BoardColumnsDeletedDTO {
   id: UUID | null;
   boardId: UUID | null;
   name: BoardEnum;
   position: number;
   color: string;
   deletedAt: string;
}


// boardColumns response DTO
export interface BoardColumnsResponseDTO {
   id: UUID | null;
   boardId: UUID | null;
   name: BoardEnum;
   position: number;
   color: string;
}


// boardColumns update DTO
export interface BoardColumnsUpdateDTO {
   name: string;
   position: number;
   color: string;
}