package com.ol.app.accounts.infrastructure.client.mapper;

import com.ol.app.accounts.domain.Loan;
import com.ol.app.accounts.infrastructure.client.loans.LoanResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  Loan toLoan(LoanResponse loanResponse);
}
