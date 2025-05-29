package com.ol.app.cards.rest;

import com.ol.app.cards.dao.CardEntity;
import com.ol.app.cards.rest.CardController.CardRequest;
import com.ol.app.cards.rest.CardController.CardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardMapper {

  CardEntity toLoanEntity(CardRequest cardRequest);

  CardEntity updateCardEntity(@MappingTarget CardEntity cardEntity, CardRequest cardRequest);

  CardResponse toCardResponse(CardEntity cardEntity);
}
