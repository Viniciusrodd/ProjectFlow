
// imports
import { ipcMain } from "electron";

// import api client
import { ApiClient } from "@main/api/apiClient";

// import types
import { iApiRequest, iApiResponse, iApiFile } from "@preload/types/api.types";


export class ApiIpc {

   // properties
   private apiClient: ApiClient;

   constructor(apiClient: ApiClient) {
      this.apiClient = apiClient;
   }


   // request
   public request() {
      ipcMain.handle(
         'api:request', 
         async (_event, data: iApiRequest): Promise<iApiResponse<unknown>> => {
            return await this.apiClient.request(data);
         }
      );
   }


   // request file
   public requestFile() {
      ipcMain.handle(
         'api:requestFile', 
         async (_event, data: iApiRequest): Promise<iApiResponse<iApiFile>> => {
            return await this.apiClient.requestFile(data);
         }
      );
   }

}