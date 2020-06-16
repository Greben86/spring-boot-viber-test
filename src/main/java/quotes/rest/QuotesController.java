package quotes.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class QuotesController {

    @GetMapping("/getAll")
    public ResponseEntity<String> getAll() {
        return new ResponseEntity<String>("Test", HttpStatus.OK);
    }
}
