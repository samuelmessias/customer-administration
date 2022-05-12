package com.samuelmessias.webservice.custumeradminitration.soup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.samuelmessias.webservice.custumeradminitration.bean.Customer;
import com.samuelmessias.webservice.custumeradminitration.services.CustomerDetailService;
import com.samuelmessias.webservice.custumeradminitration.soup.exception.CustomerNotFoundException;

import br.com.samuel.CustomerDetail;
import br.com.samuel.DeleteCustomerRequest;
import br.com.samuel.DeleteCustomerResponse;
import br.com.samuel.GetAllCustomerDetailRequest;
import br.com.samuel.GetAllCustomerDetailResponse;
import br.com.samuel.GetCustomerDetailRequest;
import br.com.samuel.GetCustomerDetailResponse;
import br.com.samuel.InsertCustomerRequest;
import br.com.samuel.InsertCustomerResponse;

@Endpoint
public class CustomerDetailPoint {
	
	@Autowired
	private CustomerDetailService service;
	
	@PayloadRoot(namespace = "http://samuel.com.br", localPart = "GetCustomerDetailRequest")
	@ResponsePayload
	public GetCustomerDetailResponse processCustomerDetailResponse(@RequestPayload GetCustomerDetailRequest req) throws Exception {
		
		Customer customer = service.findById(req.getId());
		if(customer == null) {
			throw new CustomerNotFoundException("Invalid Customer id" + req.getId());
		}
			
		return converToGetCustomerDetailResponse(customer);		
	}
	
	private GetCustomerDetailResponse converToGetCustomerDetailResponse(Customer customer) {
		GetCustomerDetailResponse resp = new GetCustomerDetailResponse();
		resp.setCustomerDetail(convertToCustomerDetail(customer));
		return resp;
	}
	
	private CustomerDetail convertToCustomerDetail(Customer customer) {
		CustomerDetail customerDetail = new CustomerDetail();
		customerDetail.setId(customer.getId());
		customerDetail.setName(customer.getName());
		customerDetail.setPhone(customer.getPhone());
		customerDetail.setEmail(customer.getEmail());
		return customerDetail;
		
	}
	
	
	@PayloadRoot(namespace = "http://samuel.com.br", localPart = "GetAllCustomerDetailRequest")
	@ResponsePayload
	public GetAllCustomerDetailResponse processGetAllCustomerDetailRequest(@RequestPayload GetAllCustomerDetailRequest req) {
		List<Customer> customers = service.findAll();
		return convertToGetAllCustomerDetailResponse(customers);
	}
	
	private GetAllCustomerDetailResponse convertToGetAllCustomerDetailResponse(List<Customer> customer) {
		GetAllCustomerDetailResponse resp = new GetAllCustomerDetailResponse();		
		customer.stream().forEach(c -> resp.getCustomerDetail().add(convertToCustomerDetail(c)));
		return resp;
	}
	
	@PayloadRoot(namespace = "http://samuel.com.br", localPart = "DeleteCustomerRequest")
	@ResponsePayload
	public DeleteCustomerResponse deleteCustomerRequest(@RequestPayload DeleteCustomerRequest req) {
		DeleteCustomerResponse resp = new DeleteCustomerResponse();
		resp.setStatus(convertStatusSoap(service.deleteById(req.getId())));
		return resp;
	}
	
	private br.com.samuel.Status convertStatusSoap(com.samuelmessias.webservice.custumeradminitration.helper.Status statusService){
		if(statusService == com.samuelmessias.webservice.custumeradminitration.helper.Status.FAILURE) {
			return  br.com.samuel.Status.FAILURE;
		}
		
		return  br.com.samuel.Status.SUCCESS;		
	}
	
	@PayloadRoot(namespace = "http://samuel.com.br", localPart = "InsertCustomerRequest")
	@ResponsePayload
	public InsertCustomerResponse insertCustomerRequest(@RequestPayload InsertCustomerRequest req) {
		InsertCustomerResponse resp = new InsertCustomerResponse();
		resp.setStatus(convertStatusSoap(service.insert(convertCustomer(req.getCustomerDetail()))));
		return resp;
	}
	
	private Customer convertCustomer(CustomerDetail customerDetail) {
		return new Customer(customerDetail.getId(), customerDetail.getName(), customerDetail.getPhone(), customerDetail.getEmail());
	}

}
