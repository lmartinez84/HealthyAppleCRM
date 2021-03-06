package ar.com.healthyapple.crm_web.service.Quote;

import ar.com.healthyapple.crm_web.exceptions.QuoteNotFoundException;
import ar.com.healthyapple.crm_web.exceptions.QuoteOperationNotAllowedException;
import ar.com.healthyapple.crm_web.exceptions.QuoteVersionMismatchException;
import ar.com.healthyapple.crm_web.model.Quote.Quote;
import ar.com.healthyapple.crm_web.model.Quote.QuoteState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ModifiedQuoteService implements QuoteStateBasedService {

    private Quote quote;

    private Quote updatedQuote;

    private QuoteOperationsFacade operations;

    @Autowired
    public ModifiedQuoteService(QuoteOperationsFacade operations) {
        this.operations = operations;
    }

    @Override
    public boolean isResponsibleFor(QuoteState quoteState) {
        return quoteState.equals(QuoteState.MODIFIED);
    }

    @Override
    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public void cancel() throws QuoteOperationNotAllowedException {
        assert this.quote != null : "Quote could not be null";
        operations.cancel(quote);
        this.updatedQuote = operations.getQuote();
    }

    @Override
    public void modify() throws QuoteOperationNotAllowedException, QuoteNotFoundException, QuoteVersionMismatchException {
        assert this.quote != null : "Quote could not be null";
        operations.modify(quote);
        this.updatedQuote = operations.getQuote();
    }
    @Override
    public void send() throws QuoteOperationNotAllowedException, QuoteNotFoundException {
        assert this.quote != null : "Quote could not be null";
        operations.send(quote);
        this.updatedQuote = operations.getQuote();
    }

    @Override
    public List<String> getAvailableOperations() {
        return Arrays.asList("cancel", "modify", "send");
    }

    @Override
    public Quote getQuote() {
        return this.updatedQuote;
    }

}
