package com.ol.app.accounts.infrastructure.rest.controller;

import java.net.URI;

import com.ol.app.accounts.api.AccountsApi;
import com.ol.app.accounts.domain.AccountService;
import com.ol.app.accounts.dto.AccountResponse;
import com.ol.app.accounts.dto.CreateAccountRequest;
import com.ol.app.accounts.dto.UpdateAccountRequest;
import com.ol.app.accounts.infrastructure.rest.mapper.AccountRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
class AccountController implements AccountsApi {

  private final AccountService accountService;
  private final AccountRestMapper accountRestMapper;

  @Override
  public ResponseEntity<Void> createAccount(CreateAccountRequest request) {
    var account = accountService.create(accountRestMapper.toAccount(request));
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{account_id}")
        .buildAndExpand(account.id()).toUri();
    return ResponseEntity.created(location).build();
  }

  @Override
  public ResponseEntity<Void> deleteAccountById(Long accountId) {
    accountService.delete(accountId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<AccountResponse> getAccountByEmail(String email) {
    return ResponseEntity.ok(accountRestMapper.toAccountResponse(accountService.findByEmail(email)));
  }

  @Override
  public ResponseEntity<AccountResponse> getAccountById(Long accountId) {
    return ResponseEntity.ok(accountRestMapper.toAccountResponse(accountService.findById(accountId)));
  }

  @Override
  public ResponseEntity<Void> updateAccountById(Long accountId, UpdateAccountRequest request) {
    accountService.update(accountRestMapper.toAccount(request, accountId));
    return ResponseEntity.noContent().build();
  }
}
