package viber.bot.services;

import org.springframework.http.ResponseEntity;
import viber.bot.model.AccountInfo;
import viber.bot.model.Message;
import viber.bot.model.ViberMessageIn;

public interface ViberService {

    ResponseEntity<String> setWebhook();

    ResponseEntity<String> removeWebHook();

    ResponseEntity<AccountInfo> getAccountInfo();

    ResponseEntity<String> botProcess(ViberMessageIn message);

    ResponseEntity<String> sentMessages(Message message);
}
