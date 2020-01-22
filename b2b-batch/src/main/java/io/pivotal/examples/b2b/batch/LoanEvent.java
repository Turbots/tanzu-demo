package io.pivotal.examples.b2b.batch;

/**
 * LoanEvent
 */
public class LoanEvent {

    private Long loanNumber;

    public LoanEvent()  {};
    public LoanEvent(Long loanNumber) {
        this.loanNumber = loanNumber;
    }

    public Long getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(Long loanNumber) {
        this.loanNumber = loanNumber;
    }
    
}