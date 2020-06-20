package quotes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import quotes.services.ViberService;
import quotes.services.ViberServiceImpl;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ViberService viberService() {
        return new ViberServiceImpl();
    }
}
