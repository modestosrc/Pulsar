/**
 * CardController.java
 * @author Mateus da Silva
*/

package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Card;
import mateus.pulsar.service.ColService;

/**
 * <h3>CardController pode ser usado com:</h3>
 * <p>
 * - GET /card/get?col=0
 * <p>
 * - POST /card/create?col=0
 * 
 * <h3>Com curl (exemplo):</h3>
 * <p>
 * - curl localhost:8080/card/get?col=0 -H "Content-Type: text/plain"
 * <p>
 * - curl localhost:8080/card/create?col=0 -H "Content-Type: text/plain" -d
 * "Card content"
 */
@RestController
@RequestMapping("/col")
public class ColController {

    private final ColService colService;

    public ColController(ColService ColService) {
        this.colService = ColService;
    }

    @GetMapping("/get")
    public String getCards(
            @RequestParam(value = "user", defaultValue = "test_user") String user,
            @RequestParam(value = "col") int coln) {
        return colService.getCol(user, coln).getCardAsJson();
    }

    @PostMapping("/create")
    public void createCard(
            @RequestParam(value = "user", defaultValue = "test_user") String user,
            @RequestParam(value = "col") int colN,
            @RequestBody String cardContent) {
            colService.addCard(user, colN, new Card(cardContent));

    }

    @DeleteMapping("/delete")
    public void deleteCard(
            @RequestParam(value = "user", defaultValue = "test_user") String user,
            @RequestParam(value = "col", defaultValue = "0") int colN,
            @RequestParam(value = "index", defaultValue = "0") int index) {
        colService.removeCard(user, colN, index);
    }

    @PutMapping("/update")
    public void updateCard(
            @RequestParam(value = "user", defaultValue = "test_user") String user,
            @RequestParam(value = "col", defaultValue = "0") int colN,
            @RequestParam(value = "index", defaultValue = "0") int index,
            @RequestBody String newContent) {
        colService.setCardContent(user, colN, index, newContent);
    }

    @PutMapping("/move")
    public void movecard(
            @RequestParam(value = "user", defaultValue = "test_user") String user,
            @RequestParam(value = "col") int coln,
            @RequestParam(value = "newCol") int coln2,
            @RequestParam(value = "index") int index,
            @RequestParam(value = "newIndex") int newIndex) {
        colService.moveCardToCol(user, coln, coln2, index, newIndex);
    }

}
