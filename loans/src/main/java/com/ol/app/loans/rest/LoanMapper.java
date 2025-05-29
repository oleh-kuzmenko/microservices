package com.ol.app.loans.rest;

import com.ol.app.loans.dao.LoanEntity;
import com.ol.app.loans.rest.LoanController.LoanRequest;
import com.ol.app.loans.rest.LoanController.LoanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LoanMapper {

  LoanEntity toLoanEntity(LoanRequest loanRequest);

  LoanEntity updateLoanEntity(@MappingTarget LoanEntity loanEntity, LoanRequest loanRequest);

  LoanResponse toLoanResponse(LoanEntity loanEntity);
}
