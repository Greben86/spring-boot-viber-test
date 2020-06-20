package quotes.services;

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
import quotes.model.*;

@Service
@PropertySource(value= {"classpath:application.properties"})
public class ViberServiceImpl implements ViberService {
    private static final String TOKEN_HEADER_NAME = "X-Viber-Auth-Token";

    @Value("${viber.bot.token}")
    private String botToken;

    @Value("${viber.bot.url")
    private String botUrl;

    @Value("${viber.set_webhook.url}")
    private String setWebhookUrl;

    @Value("${viber.account_info.url}")
    private String accountInfoUrl;

    @Value("${viber.send_message.url}")
    private String sendMessageUrl;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(TOKEN_HEADER_NAME, botToken);
        return httpHeaders;
    }

    private ResponseEntity<String> sentMessage(String receiverId, String message) {
        ViberMessageOut viberMessageOut = new ViberMessageOut();
        viberMessageOut.setReceiver(receiverId);
        viberMessageOut.setType("text");
        viberMessageOut.setText(message);

        HttpEntity<ViberMessageOut> entity = new HttpEntity<>(viberMessageOut, getHeaders());
        return restTemplate.exchange(sendMessageUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> setWebhook() {
        WebHookInfo webHookInfo = new WebHookInfo();
        webHookInfo.setUrl(botUrl);
        webHookInfo.setEvent_types(new String[]{"subscribed", "unsubscribed", "delivered", "message", "seen"});

        HttpEntity<WebHookInfo> entity = new HttpEntity<>(webHookInfo, getHeaders());
        return restTemplate.exchange(setWebhookUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<String> removeWebHook() {
        String data = "{\"url\": \""+botUrl+"\"}";
        HttpEntity<String> entity = new HttpEntity<>(data, getHeaders());
        return restTemplate.exchange(setWebhookUrl, HttpMethod.POST, entity, String.class);
    }

    @Override
    public ResponseEntity<AccountInfo> getAccountInfo() {
        HttpEntity<String> entity = new HttpEntity<>("{}", getHeaders());
        return restTemplate.exchange(accountInfoUrl, HttpMethod.POST, entity, AccountInfo.class);
    }

    @Override
    public ResponseEntity<String> botProcess(ViberMessageIn message) {
        if ("webhook".equals(message.getEvent())) {
            String data = "{\"status\": 0,\"status_message\": \"ok\",\"event_types\": [\"subscribed\", \"unsubscribed\", \"delivered\", \"message\", \"seen\"]}";
            return new ResponseEntity<>(data, HttpStatus.OK);
        } else
        if ("message".equals(message.getEvent())) {
            return sentMessage(message.getSender().getId(), "echo: "+message.getMessage().getText());
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> sentMessages(Message message) {
        getAccountInfo().getBody().getMembers().forEach(member -> sentMessage(member.getId(), message.getText()));

        return new ResponseEntity<>("", HttpStatus.OK);
    }

}