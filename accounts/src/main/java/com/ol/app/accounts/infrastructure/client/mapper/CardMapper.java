package com.ol.app.accounts.infrastructure.client.mapper;

import com.ol.app.accounts.domain.Card;
import com.ol.app.accounts.infrastructure.client.cards.CardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

  Card toCard(CardResponse cardResponse);

}
