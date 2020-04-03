package io.pivotal.examples.b2b.batch;

/**
 * LoanRates
 */
public class LoanRate {

    private Long loanNumber;
    private double newLoanRate;

    public LoanRate() {

    }

    public LoanRate(Long loanNumber, double newLoanRate)   {
        this.loanNumber = loanNumber;
        this.newLoanRate = newLoanRate;
    }

    public Long getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(Long loanNumber) {
        this.loanNumber = loanNumber;
    }

    public double getNewLoanRate() {
        return newLoanRate;
    }

    public void setNewLoanRate(double newLoanRate) {
        this.newLoanRate = newLoanRate;
    }
    
}