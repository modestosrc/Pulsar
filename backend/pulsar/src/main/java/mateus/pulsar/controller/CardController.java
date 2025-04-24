package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Card;
import mateus.pulsar.model.Col;

@RestController
@RequestMapping("/card")
public class CardController {

    @GetMapping("/get")
    public String getCard() {
        return "Card details";
    }

    @PostMapping("/create")
    public void createCard(@RequestParam(value = "col", defaultValue = "0") String colN , @RequestBody String cardContent) {
        Card card = new Card(cardContent);
        Col col = new Col("Column " + colN, Integer.parseInt(colN));
        col.addCard(card);
        col.save();
        System.out.println("Card created in column: " + col.getCards());
    }
}
