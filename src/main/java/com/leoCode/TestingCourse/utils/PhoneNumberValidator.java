package com.leoCode.TestingCourse.utils;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class PhoneNumberValidator implements Predicate<String> {

    @Override
    public boolean test(String phoneNumber) {
        //validates phone numbers having 10 digits (9998887776)
        if (phoneNumber.matches("\\d{10}")) {
            return true;
            //validates phone numbers having digits, -, . or spaces
        }else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }else if (phoneNumber.matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}")) {
            return true;
            //validates phone numbers having digits and extension (length 3 to 5)
        }else if (phoneNumber.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
            //validates phone numbers having digits and area code in braces
        }else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) {
            return true;
        }else if (phoneNumber.matches("\\(\\d{5}\\)-\\d{3}-\\d{3}")) {
            return true;
        }else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}")) {
            return true;
            //return false if any of the input matches is not found
        }else {
            return false;
        }
    }
}
