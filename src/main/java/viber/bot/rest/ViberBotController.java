package viber.bot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import viber.bot.dao.Receiver;
import viber.bot.dao.ReceiverRepository;
import viber.bot.model.AccountInfo;
import viber.bot.model.Message;
import viber.bot.model.ViberMessageIn;
import viber.bot.services.ReceiverService;
import viber.bot.services.ViberService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class ViberBotController {

    @Autowired
    private ViberService viberService;

    @Autowired
    private ReceiverService receiverService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("ping", HttpStatus.OK);
    }

    @GetMapping(value = "/setwebhook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> setWebHook() {
        return viberService.setWebhook();
    }

    @GetMapping(value = "/removewebhook", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> removeWebHook() {
        return viberService.removeWebHook();
    }

    @PostMapping(value = "/bot", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> botProcess(@RequestBody ViberMessageIn message) {
        return viberService.botProcess(message);
    }

    @GetMapping(value = "/accountinfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AccountInfo> getAccountInfo() {
        return viberService.getAccountInfo();
    }

    @PostMapping(value = "/sent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> sentMessage(@RequestBody Message message) {
        return viberService.sentMessages(message);
    }

    @GetMapping(value = "/getdata", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getData() {
        receiverService.addReceiver(new Receiver("111", "222"));
        return new ResponseEntity<>(receiverService.getAllReceivers().stream().map(Receiver::getName).collect(Collectors.joining()), HttpStatus.OK);
    }
}
