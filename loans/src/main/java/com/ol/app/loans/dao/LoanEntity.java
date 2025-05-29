package com.ol.app.loans.dao;

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

import java.time.OffsetDateTime;

@Entity
@Getter @Setter
@Table(name = "loans")
@EntityListeners(AuditingEntityListener.class)
public class LoanEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String phone;

  private String loanNumber;

  private String loanType;

  private Integer totalLoan;

  private Integer amountPaid;

  private String address;

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
