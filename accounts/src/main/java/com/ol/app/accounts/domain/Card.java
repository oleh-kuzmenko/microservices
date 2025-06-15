package com.ol.app.accounts.domain;

import java.time.OffsetDateTime;

public record Card(Integer id,
                   String phone,
                   String cardNumber,
                   String cardType,
                   Integer totalLimit,
                   Integer amountUsed,
                   Integer availableAmount,
                   OffsetDateTime createdAt,
                   String createdBy,
                   OffsetDateTime updatedAt,
                   String updatedBy) {

}
