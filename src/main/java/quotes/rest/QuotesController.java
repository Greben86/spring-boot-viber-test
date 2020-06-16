package quotes.rest;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import quotes.model.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/")
public class QuotesController {

    private static final String TOKEN = "4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f";

    @GetMapping("/ping")
    public ResponseEntity<String> getAll() {
        return new ResponseEntity<String>("ping", HttpStatus.OK);
    }

    @GetMapping(value = "/setwebhook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> setWebHook() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-Viber-Auth-Token", TOKEN);

        WebHookInfo webHookInfo = new WebHookInfo();
        webHookInfo.setUrl("https://springviberbotexample.herokuapp.com/bot");
        webHookInfo.setEvent_types(new String[]{"subscribed", "unsubscribed", "delivered", "message", "seen"});

        HttpEntity<WebHookInfo> entity = new HttpEntity<>(webHookInfo, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("https://chatapi.viber.com/pa/set_webhook", HttpMethod.POST, entity, String.class);
    }

    @GetMapping(value = "/removewebhook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> removeWebHook() throws IOException {
        String data = "{\"auth_token\": \""+TOKEN+"\", \"url\": \"https://springviberbotexample.herokuapp.com/bot\"}";

        URLConnection connection = new URL("https://chatapi.viber.com/pa/set_webhook").openConnection();
        connection.setDoOutput(true); // Triggers POST.
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream output = connection.getOutputStream()) {
            output.write(data.getBytes(StandardCharsets.UTF_8));
        }

        InputStream response = connection.getInputStream();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (response, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        return new ResponseEntity<>(textBuilder.toString(), HttpStatus.OK);

//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.add("X-Viber-Auth-Token", TOKEN);
//
//        WebHookInfo webHookInfo = new WebHookInfo();
//        webHookInfo.setAuth_token("4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f");
//        webHookInfo.setUrl("https://springviberbotexample.herokuapp.com/bot");
//        webHookInfo.setEvent_types(new String[]{"subscribed", "unsubscribed", "delivered", "message", "seen"});
//
//        return restTemplate.exchange("https://chatapi.viber.com/pa/set_webhook", HttpMethod.POST,
//                new HttpEntity<>("{\"auth_token\": \"4ba79f3c9667decd-ef68e680e88e3427-ad0a609cfd0deb1f\", \"url\": \"https://springviberbotexample.herokuapp.com/bot\", \"event_types\": [\"subscribed\", \"unsubscribed\", \"delivered\", \"message\", \"seen\"]}", httpHeaders), String.class);

//        return restTemplate.postForEntity("https://chatapi.viber.com/pa/set_webhook", webHookInfo, String.class);
    }

    @PostMapping(value = "/bot", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> botProcess(@RequestBody ViberMessageIn message) {
        if ("webhook".equals(message.getEvent())) {
            String data = "{\"status\": 0,\"status_message\": \"ok\",\"event_types\": [\"subscribed\", \"unsubscribed\", \"delivered\", \"message\", \"seen\"]}";

            return new ResponseEntity<>(data, HttpStatus.OK);
        } else
        if ("message".equals(message.getEvent())) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("X-Viber-Auth-Token", TOKEN);

            ViberMessageOut viberMessageOut = new ViberMessageOut();
            viberMessageOut.setReceiver(message.getSender().getId());
            viberMessageOut.setType("text");
            viberMessageOut.setText(message.getMessage().getText());

            HttpEntity<ViberMessageOut> entity = new HttpEntity<>(viberMessageOut, httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.exchange("https://chatapi.viber.com/pa/send_message", HttpMethod.POST, entity, String.class);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping(value = "/accountinfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getAccountInfo() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-Viber-Auth-Token", TOKEN);

        HttpEntity<String> entity = new HttpEntity<>("{}", httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange("https://chatapi.viber.com/pa/get_account_info", HttpMethod.POST, entity, String.class);
    }

    @PostMapping(value = "/sent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AccountInfo> sentMessage(@RequestBody Message message) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("X-Viber-Auth-Token", TOKEN);

        HttpEntity<String> entity = new HttpEntity<>("{}", httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AccountInfo> response = restTemplate
                .exchange("https://chatapi.viber.com/pa/get_account_info", HttpMethod.POST, entity, AccountInfo.class);

        response.getBody().getMembers().stream().forEach(member -> {
            ViberMessageOut viberMessageOut = new ViberMessageOut();
            viberMessageOut.setReceiver(member.getId());
            viberMessageOut.setType(message.getType());
            viberMessageOut.setText(message.getText());

            restTemplate.exchange("https://chatapi.viber.com/pa/send_message", HttpMethod.POST,
                    new HttpEntity<>(viberMessageOut, httpHeaders), String.class);
        });

        return response;
    }
}
