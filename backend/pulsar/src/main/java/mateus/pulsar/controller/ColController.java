/**
 * CardController.java
 * @author Mateus da Silva
*/

package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Col;
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
@RequestMapping("/{user}")
public class ColController {

    private final ColService colService;

    public ColController(ColService ColService) {
        this.colService = ColService;
    }

    @GetMapping("/coluna")
    Col[] getAllCols(@PathVariable String user) {
        return colService.getAllCols(user);
    }

    @GetMapping("/coluna/{n}")
    Col getCards(
            @PathVariable String user,
            @PathVariable int n) {
        return colService.getCol(user, n);
    }

    @PostMapping("/coluna")
    Col createCard(
            @PathVariable String user) {
        Col col = colService.createCol(user);
        return col;
    }

    @PutMapping("/coluna/{n}")
    Col setCard(
            @PathVariable String user,
            @PathVariable int n,
            @RequestBody Col col) {
        colService.setCol(user, n, col);
        return col;
    }

    @DeleteMapping("/coluna/{n}")
    void deleteCard(
            @PathVariable String user,
            @PathVariable int n) {
        colService.deleteCol(user, n);
    }
}
