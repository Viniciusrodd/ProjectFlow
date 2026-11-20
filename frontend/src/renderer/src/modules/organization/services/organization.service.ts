
// import types
import { iApiRequest, iApiResponse } from "@shared/types/api/api.types";
import { 
   OrganizationDTO, OrganizationDeletedDTO, 
   OrganizationResponseDTO, OrganizationUpdateDTO,
   UUID,
   iOrganizationMethods 
} from "@renderer/src/modules/organization/types/organizationTypes/organization.types";


class OrganizationService implements iOrganizationMethods {

   // creation
   async createOrganization(data: OrganizationDTO): Promise<iApiResponse<OrganizationResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'POST',
         path: 'organization',
         body: data,
         private: true
      };

      return await window.api.request<OrganizationResponseDTO>(apiRequest);
   }

   // get all
   async getAllOrganizations(): Promise<iApiResponse<OrganizationResponseDTO[]>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: 'organizations',
         private: true
      };

      return await window.api.request<OrganizationResponseDTO[]>(apiRequest);
   }

   // get by id
   async getById(id: UUID | null): Promise<iApiResponse<OrganizationResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `organization/${id}`,
         private: true
      };

      return await window.api.request<OrganizationResponseDTO>(apiRequest);
   }

   // get by owner
   async getByOwnerId(ownerId: UUID | null): Promise<iApiResponse<OrganizationResponseDTO[]>> {
      const apiRequest: iApiRequest = {
         method: 'GET',
         path: `organizations/${ownerId}`,
         private: true
      };

      return await window.api.request<OrganizationResponseDTO[]>(apiRequest);
   }

   // update
   async updateOrganization(
      id: UUID | null, 
      userId: UUID | null, 
      data: OrganizationUpdateDTO
   ): Promise<iApiResponse<OrganizationResponseDTO>> {
      const apiRequest: iApiRequest = {
         method: 'PUT',
         path: `organization/${id}/user/${userId}`,
         body: data,
         private: true
      };

      return await window.api.request<OrganizationResponseDTO>(apiRequest);
   }

   // delete
   async deleteOrganization(
      id: UUID | null, 
      userId: UUID | null
   ): Promise<iApiResponse<OrganizationDeletedDTO>> {
      const apiRequest: iApiRequest = {
         method: 'DELETE',
         path: `organization/${id}/user/${userId}`,
         private: true
      };

      return await window.api.request<OrganizationDeletedDTO>(apiRequest);
   }

}
export const organizationService: OrganizationService = new OrganizationService();