package com.ol.app.accounts.infrastructure.client.cards;

import com.ol.app.accounts.domain.Card;
import com.ol.app.accounts.domain.CardRepository;
import com.ol.app.accounts.infrastructure.client.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class CardRepositoryImpl implements CardRepository {

  private final CardsClient cardsClient;
  private final CardMapper cardMapper;

  @Override
  public Card findByPhone(String phone) {
    return cardMapper.toCard(cardsClient.getCard(phone));
  }
}
