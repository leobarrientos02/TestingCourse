package com.leoCode.TestingCourse.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/customer-registration")
public class CustomerRegistrationController {

    private final CustomerRegistrationService customerRegistrationService;
    @Autowired
    public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
    }

    @PostMapping
    public void registerNewCustomer(
           @RequestBody CustomerRegistrationRequest request){
        customerRegistrationService.registerNewCustomer(request);
    }
}
