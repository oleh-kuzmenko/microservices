package com.ol.app.accounts.infrastructure.client.cards;

import java.time.OffsetDateTime;

public record CardResponse(Integer id,
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
