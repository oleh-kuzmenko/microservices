package com.ol.app.accounts.domain;

import java.util.Optional;

public interface AccountRepository {

  Account save(Account account);

  Optional<Account> findById(Long accountId);

  Optional<Account> findByEmail(String email);

  Optional<Account> findByPhone(String phone);

  void delete(Long accountId);

  boolean existsByEmailOrPhone(String email, String phone);
}
