package com.samuelmessias.webservice.custumeradminitration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samuelmessias.webservice.custumeradminitration.bean.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
