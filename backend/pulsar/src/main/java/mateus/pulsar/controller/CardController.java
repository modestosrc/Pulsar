/**
 * CardController.java
 * @author Mateus da Silva
*/

package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Card;
import mateus.pulsar.model.Col;

/**
 * CardController pode ser usado com:
 * - GET /card/get?col=0
 * - POST /card/create?col=0
 * 
 * Com curl (exemplo):
 * - curl localhost:8080/card/get?col=0 -H "Content-Type: text/plain"
 * - curl localhost:8080/card/create?col=0 -H "Content-Type: text/plain" -d
 * "Card content"
 */
@RestController
@RequestMapping("/card")
public class CardController {

    @GetMapping("/get")
    public String getCards(@RequestParam(value = "col", defaultValue = "0") String colN) {
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, 0);
        return col.getCards();
    }

    @PostMapping("/create")
    public void createCard(@RequestParam(value = "col", defaultValue = "0") String colN,
            @RequestBody String cardContent) {
        Card card = new Card(cardContent);
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, 0);
        col.addCard(card);
        col.save();
        System.out.println("Card created in column " + colN + ": " + cardContent);
    }
}
