
// packages
package com.example.ProjectFlow.modules.notification.repository;

// imports
import java.util.List;
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.notification.document.NotificationDocument;


public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
 
   // find all notifications by user id
   List<NotificationDocument> findAllByUserId(UUID userId);

   // find all notifications by user id not readed
   List<NotificationDocument> findAllByUserNotReaded(UUID userId, boolean read);
   
   // find all notifications by read field
   List<NotificationDocument> findAllByRead(boolean read);

   // exists by user id
   boolean existsByUserId(UUID userId);

   // delete all by user id
   void deleteAllByUserId(UUID userId);

}