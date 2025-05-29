package com.ol.app.loans.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

  Optional<LoanEntity> findByPhone(String phone);
}
