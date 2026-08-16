
// packages
package com.example.ProjectFlow.modules.organization.entity;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;

// jakarta imports
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;

// import interfaces
import com.example.ProjectFlow.common.interfaces.crudBase.SoftDeleteInterface;

// import entities
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import enums
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@Entity
@Table(
   name = "organization_members",
   uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "organization_id" }) }
)
@SQLRestriction("deleted_at IS NULL")
public class OrganizationMembersEntity implements SoftDeleteInterface {
 
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   @Column(columnDefinition = "VARCHAR(36)", length = 36)
   @JdbcTypeCode(java.sql.Types.VARCHAR)
   private UUID id;

   // N(users) : 1(organization_members)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private UserEntity user;
   
   // N(organizations) : 1(organization_members)
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "organization_id", nullable = false)
   private OrganizationEntity organization;
   
   
   //// fields
   

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private RoleEnum role;

   @CreationTimestamp
   @Column(name = "joined_at", updatable = false)
   private LocalDateTime joinedAt;

   @UpdateTimestamp
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;


   // constructor - empty
   public OrganizationMembersEntity() {} 


   // constructor - builder
   public OrganizationMembersEntity(Builder builder) {
      setUser(builder.user);
      setOrganization(builder.organization);
      setRole(builder.role);
      setJoinedAt(builder.joinedAt);
   }


   // getters
   public UUID getId() { return this.id; }
   public OrganizationEntity getOrganization() { return this.organization; }
   public UUID getOrganizationId() { return this.organization.getId(); }
   public UserEntity getUser() { return this.user; }
   public UUID getUserId() { return this.user.getId(); }   
   public RoleEnum getRole() { return this.role; }
   public LocalDateTime getJoinedAt() { return this.joinedAt; }
   public LocalDateTime getUpdatedAt() { return this.updatedAt; }
   public LocalDateTime getDeletedAt() { return this.deletedAt; }


   // setters
   public void setId(UUID id) { this.id = id; }
   public void setOrganization(OrganizationEntity organization) { this.organization = organization; }
   public void setUser(UserEntity user) { this.user = user; }
   public void setRole(RoleEnum role) { this.role = role; }
   public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
   public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
   public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }


   // utils
   public boolean isDeleted() { return this.deletedAt != null; }


   //// builder


   public static class Builder {
      private UserEntity user;
      private OrganizationEntity organization;
      private RoleEnum role;
      private LocalDateTime joinedAt;

      public Builder user(UserEntity user) {
         this.user = user;
         return this;
      }

      public Builder organization(OrganizationEntity organization) {
         this.organization = organization;
         return this;
      }

      public Builder role(RoleEnum role) {
         this.role = role;
         return this;
      }

      public Builder joinedAt(LocalDateTime joinedAt) {
         this.joinedAt = joinedAt;
         return this;
      }

      public OrganizationMembersEntity build() {
         return new OrganizationMembersEntity(this);
      }
   }

}