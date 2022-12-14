package com.leoCode.TestingCourse.customer;

import com.leoCode.TestingCourse.utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository, PhoneNumberValidator phoneNumberValidator) {
        this.customerRepository = customerRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request){
        // 1. Check if PhoneNumber is taken
        String phoneNumber = request.getCustomer().getPhoneNumber();

        if(!phoneNumberValidator.test(phoneNumber)){
            throw new IllegalStateException("Phone number " + phoneNumber + " is not valid");
        }

        Optional<Customer> optionalCustomer = customerRepository.selectCustomerByPhoneNumber(phoneNumber);
        // 2. If taken lets check if it belongs to same customer
        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            // - 2.1 If yes return
            if(customer.getName().equals(request.getCustomer().getName())){
                return;
            }
            // - 2.2 throw an exception
            throw new IllegalStateException(String.format("Phone number %s is taken",phoneNumber));
        }
        if(request.getCustomer().getId() == null){
            request.getCustomer().setId(UUID.randomUUID());
        }
        // 3. save customer
        customerRepository.save(request.getCustomer());
    }
}
