package viber.bot.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import viber.bot.config.ViberConfig;
import viber.bot.model.*;

@Service
public class ViberServiceImpl implements ViberService {
    private static final String TOKEN_HEADER_NAME = "X-Viber-Auth-Token";

    @Autowired
    private ViberConfig viberConfig;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(TOKEN_HEADER_NAME, viberConfig.getBotToken());
        return httpHeaders;
    }

    private ResponseEntity<String> sentMessage(String receiverId, String message) {
        ViberMessageOut viberMessageOut = new ViberMessageOut();
        viberMessageOut.setReceiver(receiverId);
        viberMessageOut.setType(MessageType.text);
        viberMessageOut.setText(message);

        HttpEntity<ViberMessageOut> entity = new HttpEntity<>(viberMessageOut, getHeaders());
        return restTemplate.exchange(viberConfig.getSendMessageUrl(), HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> setWebhook() {
        String jsonString = new JSONObject()
                .put("url", viberConfig.getBotUrl())
                .put("event_types", new EventTypes[]{EventTypes.subscribed, EventTypes.unsubscribed,
                        EventTypes.delivered, EventTypes.message, EventTypes.seen})
                .toString();

        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(viberConfig.getSetWebhookUrl(), HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> removeWebHook() {
        String jsonString = new JSONObject()
                .put("url", viberConfig.getBotUrl())
                .toString();
        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(viberConfig.getSetWebhookUrl(), HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<AccountInfo> getAccountInfo() {
        String jsonString = new JSONObject().toString();
        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(viberConfig.getAccountInfoUrl(), HttpMethod.POST, entity, AccountInfo.class);
    }

    @Override
    public ResponseEntity<String> botProcess(ViberMessageIn message) {
        if (EventTypes.webhook.equals(message.getEvent())) {
            String jsonString = new JSONObject()
                    .put("status", 0)
                    .put("status_message", "ok")
                    .put("event_types", new EventTypes[]{EventTypes.subscribed, EventTypes.unsubscribed,
                            EventTypes.delivered, EventTypes.message, EventTypes.seen})
                    .toString();

            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        } else
        if (EventTypes.message.equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "echo: "+message.getMessage().getText());
        } else
        if (EventTypes.subscribed.equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "Subscribed");
        } else
        if (EventTypes.unsubscribed.equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "Unsubscribed");
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sentMessages(Message message) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}