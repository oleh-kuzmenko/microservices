package com.ol.app.cards.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, Long> {

  Optional<CardEntity> findByPhone(String phone);
}
