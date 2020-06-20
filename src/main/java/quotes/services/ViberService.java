package quotes.services;

import org.springframework.http.ResponseEntity;
import quotes.model.AccountInfo;
import quotes.model.Message;
import quotes.model.ViberMessageIn;

public interface ViberService {

    ResponseEntity<String> setWebhook();

    ResponseEntity<String> removeWebHook();

    ResponseEntity<AccountInfo> getAccountInfo();

    ResponseEntity<String> botProcess(ViberMessageIn message);

    ResponseEntity<String> sentMessages(Message message);
}
