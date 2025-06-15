package com.ol.app.accounts.infrastructure.client.loans;

import java.time.OffsetDateTime;

public record LoanResponse(Integer id,
                           String phone,
                           String loanNumber,
                           String loanType,
                           Integer totalLoan,
                           Integer amountPaid,
                           String address,
                           OffsetDateTime createdAt,
                           String createdBy,
                           OffsetDateTime updatedAt,
                           String updatedBy) {

}
