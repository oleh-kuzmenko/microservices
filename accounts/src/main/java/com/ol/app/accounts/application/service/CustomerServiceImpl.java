package com.ol.app.accounts.application.service;

import com.ol.app.accounts.application.exception.NotFountException;
import com.ol.app.accounts.domain.Account;
import com.ol.app.accounts.domain.AccountRepository;
import com.ol.app.accounts.domain.Card;
import com.ol.app.accounts.domain.CardRepository;
import com.ol.app.accounts.domain.Customer;
import com.ol.app.accounts.domain.CustomerService;
import com.ol.app.accounts.domain.Loan;
import com.ol.app.accounts.domain.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final AccountRepository accountRepository;
  private final CardRepository cardRepository;
  private final LoanRepository loanRepository;

  @Override
  public Customer findByPhone(String phone) {
    Account account = accountRepository.findByPhone(phone).orElseThrow(() -> new NotFountException("Account not found"));
    Card card = cardRepository.findByPhone(phone);
    Loan loan = loanRepository.findByPhone(phone);
    return new Customer(account, card, loan);
  }
}
