
// packages
package com.example.ProjectFlow.modules.audit.document;

// imports
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// import enums
import com.example.ProjectFlow.modules.audit.enums.OperationEnum;


@Document(collection = "audit_logs")
public class AuditLogsDocument {
 
   @Id
   private String id;

   @Field("entity")
   private String entity;

   @Field("entityId")
   private UUID entityId;   
   
   @Field("operation")
   private OperationEnum operation;

   @Field("userId")
   private UUID userId;   
   
   @Field("date")
   private LocalDateTime date;   
   
   @Field("before")
   private Map<String, Object> before; // entity state
   
   @Field("after")
   private Map<String, Object> after; // entity state
   

   // constructor
   protected AuditLogsDocument() {}


   // getters
   public String getId() { return this.id; }
   public String getEntity() { return this.entity; }
   public UUID getEntityId() { return this.entityId; }
   public OperationEnum getOperation() { return this.operation; }
   public UUID getUserId() { return this.userId; }
   public LocalDateTime getDate() { return this.date; }
   public Map<String, Object> getBefore() { return this.before; }
   public Map<String, Object> getAfter() { return this.after; }
   

   // setters
   public void setId(String id) { this.id = id; }
   public void setEntity(String entity) { this.entity = entity; }
   public void setEntityId(UUID entityId) { this.entityId = entityId; }
   public void setOperation(OperationEnum operation) { this.operation = operation; }
   public void setUserId(UUID userId) { this.userId = userId; }
   public void setDate(LocalDateTime date) { this.date = date; }
   public void setBefore(Map<String, Object> entity) { this.before = entity; }
   public void setAfter(Map<String, Object> entity) { this.after = entity; }

}