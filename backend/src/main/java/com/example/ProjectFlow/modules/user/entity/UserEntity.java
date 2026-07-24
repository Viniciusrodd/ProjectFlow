
// packages
package com.example.ProjectFlow.modules.user.entity;

// imports
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDelete;;


@Entity
@Table(name = "users")
@SQLRestriction("deleted_at IS NULL") // auto. filter not deleted registers
public class UserEntity implements SoftDelete {
 
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, length = 120)
   private String name;

   @Column(nullable = false, length = 255, unique = true)
   private String email;

   @Column(nullable = false, length = 255)
   private String password;

   @Column(name = "profile_image_id")
   private String profileImageId; // mongo ref.
   
   @CreationTimestamp
   @Column(name = "created_at", updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;

   // constructor
   protected UserEntity() {}

   // getters
   public Long getId() { return this.id; }
   public String getName() { return this.name; }
   public String getEmail() { return this.email; }
   public String getPassword() { return this.password; }
   public String getProfileImageId() { return this.profileImageId; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }

   // setters
   public void setId(long id) { this.id = id; }
   public void setName(String name) { this.name = name; }
   public void setEmail(String email) { this.email = email; }
   public void setPassword(String password) { this.password = password; }
   public void setProfileImageId(String profileImageId) { this.profileImageId = profileImageId; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

   // utils
   public boolean isDeleted() { return this.deletedAt != null; }

}