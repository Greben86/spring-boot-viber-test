package quotes.rest;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import quotes.model.ViberMessageIn;
import quotes.model.ViberMessageOut;
import quotes.model.WebHookInfo;

@RestController
@RequestMapping("/")
public class QuotesController {

    @GetMapping("/ping")
    public ResponseEntity<String> getAll() {
        return new ResponseEntity<String>("ping", HttpStatus.OK);
    }

    @GetMapping(value = "/setwebhook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> setWebHook() {
        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.add("X-Viber-Auth-Token", "4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f");

        WebHookInfo webHookInfo = new WebHookInfo();
        webHookInfo.setAuth_token("4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f");
        webHookInfo.setUrl("https://springviberbotexample.herokuapp.com/bot");
        webHookInfo.setEvent_types(new String[]{"subscribed", "unsubscribed", "delivered", "message", "seen"});

        return restTemplate.postForEntity("https://chatapi.viber.com/pa/set_webhook", webHookInfo, String.class);
    }

    @GetMapping(value = "/bot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> botProcess(@RequestBody ViberMessageIn message) {
        ViberMessageOut viberMessageOut = new ViberMessageOut();
        viberMessageOut.setAuth_token("4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f");
        viberMessageOut.setReceiver(message.getSender().getId());
        viberMessageOut.setType("text");
        viberMessageOut.setText("Hello world!");

        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.add("X-Viber-Auth-Token", "4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f");

        return restTemplate.postForEntity("https://chatapi.viber.com/pa/send_message", viberMessageOut, String.class);
    }
}
