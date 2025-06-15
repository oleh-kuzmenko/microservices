package com.ol.app.accounts.domain;

import java.time.OffsetDateTime;

public record Loan(Integer id,
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
