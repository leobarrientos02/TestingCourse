package com.leoCode.TestingCourse.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request){
        // 1. Check if PhoneNumber is taken
        // 2. If taken lets check if it belongs to same customer
        // - 2.1 If yes return
        // - 2.2 throw an exception
        // 3. save customer
        String phoneNumber = request.getCustomer().getPhoneNumber();
        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if(customer.getName().equals(request.getCustomer().getName())){
                return;
            }
            throw new IllegalStateException(String.format("Phone number [%s] is taken",phoneNumber));
        }
        customerRepository.save(request.getCustomer());
    }
}
