package com.samuelmessias.webservice.custumeradminitration.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samuelmessias.webservice.custumeradminitration.bean.Customer;
import com.samuelmessias.webservice.custumeradminitration.helper.Status;
import com.samuelmessias.webservice.custumeradminitration.repositories.CustomerRepository;

@Component
public class CustomerDetailService {
	
	@Autowired
	private CustomerRepository repository;
	
	public Customer findById(Integer id) {
		Optional<Customer> customerOptional = repository.findById(id);
		if(customerOptional.isPresent()) {
			return customerOptional.get();			
		}
		return null;
	}
	
	public List<Customer> findAll() {
		return repository.findAll();
	}
	
	public Status deleteById(Integer id) {
		try {
			repository.deleteById(id);
			return Status.SUCCESS;
		}catch(Exception ex) {
			return Status.FAILURE;
		}
	}
	
	public Status insert(Customer customer) {
		try {
			repository.save(customer);
			return Status.SUCCESS;
		}catch(Exception ex) {
			return Status.FAILURE;
		}
	}

}
