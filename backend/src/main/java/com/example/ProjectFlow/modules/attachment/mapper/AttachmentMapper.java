
// packages
package com.example.ProjectFlow.modules.attachment.mapper;

// imports
import org.springframework.stereotype.Component;

// import documents
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;

// import DTOs
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;


@Component 
public class AttachmentMapper {
 
   // from AttachmentDocument to AttachmentResponseDTO
   public AttachmentResponseDTO toAttachmentResponseDTO(AttachmentDocument document) {
      return new AttachmentResponseDTO(
         document.getId(),
         document.getTaskId(),
         document.getUploadedBy(),
         document.getFileName(),
         document.getMimeType(),
         document.getSize(),
         document.getUploadDate()
      );
   }

}