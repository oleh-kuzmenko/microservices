package com.ol.app.accounts.domain;

public interface AccountService {

  Account create(Account account);

  Account findById(Long accountId);

  Account findByEmail(String email);

  void delete(Long accountId);

  void update(Account account);
}
