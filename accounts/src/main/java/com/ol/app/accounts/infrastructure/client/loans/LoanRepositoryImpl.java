package com.ol.app.accounts.infrastructure.client.loans;

import com.ol.app.accounts.domain.Loan;
import com.ol.app.accounts.domain.LoanRepository;
import com.ol.app.accounts.infrastructure.client.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LoanRepositoryImpl implements LoanRepository {

  private final LoansClient loansClient;
  private final LoanMapper loanMapper;

  @Override
  public Loan findByPhone(String phone) {
    return loanMapper.toLoan(loansClient.getLoan(phone));
  }
}
