package com.ol.app.accounts.infrastructure.rest.controller;

import com.ol.app.accounts.api.CustomersApi;
import com.ol.app.accounts.domain.Customer;
import com.ol.app.accounts.domain.CustomerService;
import com.ol.app.accounts.dto.CustomerResponse;
import com.ol.app.accounts.infrastructure.rest.mapper.CustomerRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerController implements CustomersApi {

  private final CustomerService customerService;
  private final CustomerRestMapper customerRestMapper;

  @Override
  public ResponseEntity<CustomerResponse> getCustomerByPhone(String phone) {
    Customer customer = customerService.findByPhone(phone);
    return ResponseEntity.ok(customerRestMapper.toCustomerResponse(customer));
  }
}
