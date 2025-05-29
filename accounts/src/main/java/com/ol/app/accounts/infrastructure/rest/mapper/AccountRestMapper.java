package com.ol.app.accounts.infrastructure.rest.mapper;

import com.ol.app.accounts.domain.Account;
import com.ol.app.accounts.dto.AccountResponse;
import com.ol.app.accounts.dto.CreateAccountRequest;
import com.ol.app.accounts.dto.UpdateAccountRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountRestMapper {

  Account toAccount(CreateAccountRequest request);

  AccountResponse toAccountResponse(Account account);

  Account toAccount(UpdateAccountRequest request, Long id);
}
