package com.ol.app.accounts.infrastructure.persistence.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter @Setter
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_type")
  private String accountType;

  private String address;

  private String name;

  private String email;

  private String phone;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private OffsetDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @UpdateTimestamp
  @Column(name = "updated_at", insertable = false)
  private OffsetDateTime updatedAt;

  @LastModifiedBy
  @Column(name = "updated_by", insertable = false)
  private String updatedBy;

}
