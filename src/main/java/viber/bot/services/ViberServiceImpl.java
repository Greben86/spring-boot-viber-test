package viber.bot.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import viber.bot.model.*;

@Service
@PropertySource(value= {"classpath:application.properties"})
public class ViberServiceImpl implements ViberService {
    private static final String TOKEN_HEADER_NAME = "X-Viber-Auth-Token";

    @Value("${viber.bot.token}")
    private String botToken;

    @Value("${viber.bot.url}")
    private String botUrl;

    @Value("${viber.set_webhook.url}")
    private String setWebhookUrl;

    @Value("${viber.account_info.url}")
    private String accountInfoUrl;

    @Value("${viber.send_message.url}")
    private String sendMessageUrl;

    private RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(TOKEN_HEADER_NAME, botToken);
        return httpHeaders;
    }

    private ResponseEntity<String> sentMessage(String receiverId, String message) {
        ViberMessageOut viberMessageOut = new ViberMessageOut();
        viberMessageOut.setReceiver(receiverId);
        viberMessageOut.setType(MessageType.text);
        viberMessageOut.setText(message);

        HttpEntity<ViberMessageOut> entity = new HttpEntity<>(viberMessageOut, getHeaders());
        return restTemplate.exchange(sendMessageUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> setWebhook() {
        /*WebHookInfo webHookInfo = new WebHookInfo();
        webHookInfo.setUrl(botUrl);
        webHookInfo.setEvent_types(new EventTypes[]{EventTypes.subscribed, EventTypes.unsubscribed,
                EventTypes.delivered, EventTypes.message, EventTypes.seen});*/

        String jsonString = new JSONObject()
                .put("url", botUrl)
                .put("event_types", new EventTypes[]{EventTypes.subscribed, EventTypes.unsubscribed,
                        EventTypes.delivered, EventTypes.message, EventTypes.seen})
                .toString();

        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(setWebhookUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> removeWebHook() {
        //String data = "{\"url\": \""+botUrl+"\"}";
        String jsonString = new JSONObject()
                .put("url", botUrl)
                .toString();
        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(setWebhookUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> getAccountInfo() {
        String jsonString = new JSONObject().toString();
        HttpEntity<String> entity = new HttpEntity<>(jsonString, getHeaders());
        return restTemplate.exchange(accountInfoUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> botProcess(ViberMessageIn message) {
        if (EventTypes.webhook.equals(message.getEvent())) {
            //String data = "{\"status\": 0,\"status_message\": \"ok\",\"event_types\": [\"subscribed\", \"unsubscribed\", \"delivered\", \"message\", \"seen\"]}";
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
            return sentMessage(message.getSender().getId(), "Подписался - молодец!");
        } else
        if (EventTypes.unsubscribed.equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "Зачем отписался!?");
        } else
        if (EventTypes.conversation_started.equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "Беседа началась");
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sentMessages(Message message) {
        //getAccountInfo().getBody().getMembers().forEach(member -> sentMessage(member.getId(), message.getText()));

        return new ResponseEntity<>("", HttpStatus.OK);
    }

}