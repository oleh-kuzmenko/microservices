package com.ol.app.accounts.infrastructure.rest.mapper;

import com.ol.app.accounts.domain.Customer;
import com.ol.app.accounts.dto.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerRestMapper {

  @Mapping(target = "cardNumber", source = "customer.card.cardNumber")
  @Mapping(target = "cardType", source = "customer.card.cardType")
  @Mapping(target = "totalLimit", source = "customer.card.totalLimit")
  @Mapping(target = "amountUsed", source = "customer.card.amountUsed")
  @Mapping(target = "availableAmount", source = "customer.card.availableAmount")
  @Mapping(target = "loanNumber", source = "customer.loan.loanNumber")
  @Mapping(target = "loanType", source = "customer.loan.loanType")
  @Mapping(target = "totalLoan", source = "customer.loan.totalLoan")
  @Mapping(target = "amountPaid", source = "customer.loan.amountPaid")
  @Mapping(target = "name", source = "customer.account.name")
  @Mapping(target = "email", source = "customer.account.email")
  @Mapping(target = "phone", source = "customer.account.phone")
  @Mapping(target = "accountType", source = "customer.account.accountType")
  @Mapping(target = "address", source = "customer.account.address")
  CustomerResponse toCustomerResponse(Customer customer);
}
