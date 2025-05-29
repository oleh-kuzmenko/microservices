package com.ol.app.loans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LoansApplication {

  public static void main(String[] args) {
    SpringApplication.run(LoansApplication.class, args);
  }

}
