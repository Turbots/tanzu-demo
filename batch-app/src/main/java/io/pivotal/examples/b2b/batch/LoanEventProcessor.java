package io.pivotal.examples.b2b.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * LoanEventProcessor
 */
public class LoanEventProcessor implements ItemProcessor<LoanEvent, LoanRate>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanEventProcessor.class);

    public LoanRate process(final LoanEvent loanEvent) throws Exception {
        LoanRate loanRate = new LoanRate(loanEvent.getLoanNumber(), Math.random());
        LOGGER.info("Loan {} has a new loan rate of {}", loanRate.getLoanNumber(), loanRate.getNewLoanRate());
        return loanRate;
    }
    
}