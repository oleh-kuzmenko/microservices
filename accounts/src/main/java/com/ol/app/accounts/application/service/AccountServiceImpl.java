package com.ol.app.accounts.application.service;

import com.ol.app.accounts.application.exception.NotFountException;
import com.ol.app.accounts.application.exception.NotUniqueException;
import com.ol.app.accounts.domain.Account;
import com.ol.app.accounts.domain.AccountRepository;
import com.ol.app.accounts.domain.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;

  @Override
  public Account create(Account account) {
    if (accountRepository.existsByEmailOrPhone(account.email(), account.phone())) {
      throw new NotUniqueException("Account with such email or phone already exists");
    }
    return accountRepository.save(account);
  }

  @Override
  public Account findById(Long accountId) {
    return accountRepository.findById(accountId)
        .orElseThrow(this::notFoundException);
  }

  @Override
  public Account findByEmail(String email) {
    return accountRepository.findByEmail(email)
        .orElseThrow(this::notFoundException);
  }

  private NotFountException notFoundException() {
    return new NotFountException("Account not found");
  }

  @Override
  public void delete(Long accountId) {
    accountRepository.delete(accountId);
  }

  @Override
  public void update(Account account) {
    accountRepository.findById(account.id()).ifPresentOrElse(existingAccount -> accountRepository.save(account),
        () -> {
          throw notFoundException();
        });
  }

}
