package com.bpx.loans_microservice.services;

import com.bpx.loans_microservice.models.dtos.LoanDTO;

public interface LoanService {

    void createLoan(String mobileNumber);

    LoanDTO fetchLoan(String mobileNumber);

    boolean updateLoan(LoanDTO loanDto);
    boolean deleteLoan(String mobileNumber);
}
