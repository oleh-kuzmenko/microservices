package com.ol.app.accounts.infrastructure.persistence.mapper;

import com.ol.app.accounts.domain.Account;
import com.ol.app.accounts.infrastructure.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {

  AccountEntity toAccountEntity(Account account);

  Account toAccount(AccountEntity accountEntity);

}
