package com.ol.app.accounts.domain;

public interface LoanRepository {

  Loan findByPhone(String phone);

}
