package com.bpx.loans_microservice.services;

import com.bpx.loans_microservice.constants.LoanConstants;
import com.bpx.loans_microservice.exceptions.LoanAlreadyExistsException;
import com.bpx.loans_microservice.exceptions.ResourceNotFoundException;
import com.bpx.loans_microservice.mapper.LoansMapper;
import com.bpx.loans_microservice.models.dtos.LoanDTO;
import com.bpx.loans_microservice.models.enities.Loan;
import com.bpx.loans_microservice.repositories.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loan> optionalLoans= loanRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
        }
        loanRepository.save(createNewLoan(mobileNumber));
    }

    private Loan createNewLoan(String mobileNumber) {
        Loan newLoan = new Loan();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoanConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoanConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoanConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    @Override
    public LoanDTO fetchLoan(String mobileNumber) {
        Loan loans = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoanDTO());
    }

    @Override
    public boolean updateLoan(LoanDTO loanDto) {
        Loan loan = loanRepository.findByLoanNumber(loanDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loanDto.getLoanNumber()));
        LoansMapper.mapToLoans(loanDto, loan);
        loanRepository.save(loan);
        return  true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loan loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loanRepository.deleteById(loan.getLoanId());
        return true;
    }
}
