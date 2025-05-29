package com.ol.app.accounts.domain;

import java.time.OffsetDateTime;

public record Account(Long id,
                      String name,
                      String email,
                      String phone,
                      String accountType,
                      String address,
                      String createdBy,
                      OffsetDateTime createdAt,
                      String updatedBy,
                      OffsetDateTime updatedAt) {

}
