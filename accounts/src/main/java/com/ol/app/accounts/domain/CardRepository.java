package com.ol.app.accounts.domain;

public interface CardRepository {

  Card findByPhone(String phone);

}
