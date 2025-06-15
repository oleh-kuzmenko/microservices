package com.ol.app.accounts.infrastructure.persistence.repository.impl;

import java.util.Optional;

import com.ol.app.accounts.domain.Account;
import com.ol.app.accounts.domain.AccountRepository;
import com.ol.app.accounts.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.ol.app.accounts.infrastructure.persistence.repository.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AccountRepositoryImpl implements AccountRepository {

  private final AccountJpaRepository accountJpaRepository;
  private final AccountPersistenceMapper accountPersistenceMapper;

  @Override
  public Account save(Account account) {
    return accountPersistenceMapper.toAccount(accountJpaRepository.save(accountPersistenceMapper.toAccountEntity(account)));
  }

  @Override
  public Optional<Account> findById(Long accountId) {
    return accountJpaRepository.findById(accountId).map(accountPersistenceMapper::toAccount);
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    return accountJpaRepository.findByEmail(email).map(accountPersistenceMapper::toAccount);
  }

  @Override
  public Optional<Account> findByPhone(String phone) {
    return accountJpaRepository.findByPhone(phone).map(accountPersistenceMapper::toAccount);
  }

  @Override
  public void delete(Long accountId) {
    accountJpaRepository.deleteById(accountId);
  }

  @Override
  public boolean existsByEmailOrPhone(String email, String phone) {
    return accountJpaRepository.existsByEmailOrPhone(email, phone);
  }
}
