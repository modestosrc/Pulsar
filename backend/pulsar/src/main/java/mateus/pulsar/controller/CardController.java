/**
 * CardController.java
 * @author Mateus da Silva
*/

package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Card;
import mateus.pulsar.model.Col;

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
@RequestMapping("/card")
public class CardController {

    @GetMapping("/get")
    public String getCards(
            @RequestParam(value = "col", defaultValue = "0") String colN) {
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, "test_user", 0);
        return col.getCards();
    }

    @PostMapping("/create")
    public void createCard(
            @RequestParam(value = "col", defaultValue = "0") String colN,
            @RequestBody String cardContent) {
        Card card = new Card(cardContent);
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, "test_user", 0);
        col.addCard(card);
        System.out.println("Card created in column " + colN + ": " + cardContent);
    }

    @DeleteMapping("/delete")
    public void deleteCard(
            @RequestParam(value = "col", defaultValue = "0") String colN,
            @RequestParam(value = "index", defaultValue = "0") int index) {
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, "test_user", 0);
        col.removeCard(index);
        System.out.println("Card deleted from column " + colN + ": index " + index);
    }

    @PutMapping("/update")
    public void updateCard(
            @RequestParam(value = "col", defaultValue = "0") String colN,
            @RequestParam(value = "index", defaultValue = "0") int index,
            @RequestBody String newContent) {
        Col col = new Col(Integer.parseInt(colN), "Col " + colN, "test_user", 0);
        col.updateCard(index, newContent);
        System.out.println("Card updated in column " + colN + ": index " + index + ", new content: " + newContent);
    }

    @PutMapping("/move")
    public void movecard(
            @RequestParam(value = "col") int coln,
            @RequestParam(value = "newCol") int coln2,
            @RequestParam(value = "index") int index,
            @RequestParam(value = "newIndex") int newIndex) {
        if (coln == coln2) {
            Col col = new Col(coln, "Col " + coln, "test_user", 0);
            col.moveCard(index, newIndex);
            System.out.println("Card moved in column " + coln + ": index " + index + " to index " + newIndex);
        } else {
            Col col = new Col(coln, "Col " + coln, "test_user", 0);
            Col col2 = new Col(coln2, "Col " + coln2, "test_user", 0);
            col2.addCard(col.getCard(index));
            col2.moveCard((col2.getCardCount() - 1), newIndex);
            col.removeCard(index);
            System.out.println("Card moved from column " + coln + ": index " + index + " to column " + coln2
                    + ": index " + newIndex);
        }
    }

}
