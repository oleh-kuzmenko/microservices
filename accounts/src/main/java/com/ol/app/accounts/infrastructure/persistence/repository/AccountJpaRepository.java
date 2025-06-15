package com.ol.app.accounts.infrastructure.persistence.repository;

import java.util.Optional;

import com.ol.app.accounts.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

  Optional<AccountEntity> findByEmail(String email);

  Optional<AccountEntity> findByPhone(String phone);

  boolean existsByEmailOrPhone(String email, String phone);
}
