package quotes.service;

import org.springframework.stereotype.Service;

@Service
public class QuotesServiceImpl implements QuotesService {
    @Override
    public String getResponse() {
        return "It`s alive";
    }
}
