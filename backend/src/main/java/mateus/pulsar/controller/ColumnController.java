package mateus.pulsar.controller;

import org.springframework.web.bind.annotation.*;
import mateus.pulsar.model.Column;
import mateus.pulsar.service.ColumnService;

/**
 * Controller for managing columns.
 * <p>
 * This controller handles HTTP requests related to columns, including retrieving,
 * creating, updating, and deleting columns.
 */
@RestController
@RequestMapping("/{user}")
public class ColumnController {

    /**
     * The ColumnService instance used for column operations.
     */
    private final ColumnService columnService;

    /**
     * Constructor for ColumnController.
     *
     * @param ColumnService The ColumnService instance to be used.
     */
    public ColumnController(ColumnService ColumnService) {
        this.columnService = ColumnService;
    }

    /**
     * Retrieves all columns for a specific user.
     *
     * @param user The username of the user whose columns are to be retrieved.
     * @return An array of Column objects representing the user's columns.
     */
    @GetMapping("/coluna")
    Column[] getAllColumns(@PathVariable String user) {
        return columnService.getAllColumns(user);
    }

    /**
     * Retrieves a specific column for a user.
     *
     * @param user The username of the user whose column is to be retrieved.
     * @param n    The index of the column to be retrieved.
     * @return The Column object representing the user's column.
     */
    @GetMapping("/coluna/{n}")
    Column getCards(
            @PathVariable String user,
            @PathVariable int n) {
        return columnService.getColumn(user, n);
    }

    /**
     * Creates a new column for a user.
     *
     * @param user The username of the user for whom the column is to be created.
     * @return The newly created Column object.
     */
    @PostMapping("/coluna")
    Column createCard(
            @PathVariable String user) {
        Column column = columnService.createColumn(user);
        return column;
    }

    /**
     * Updates a specific column for a user.
     *
     * @param user   The username of the user whose column is to be updated.
     * @param n      The index of the column to be updated.
     * @param column The Column object containing the updated data.
     * @return The updated Column object.
     */
    @PutMapping("/coluna/{n}")
    Column setCard(
            @PathVariable String user,
            @PathVariable int n,
            @RequestBody Column column) {
        columnService.setColumn(user, n, column);
        return column;
    }

    /**
     * Deletes a specific column for a user.
     *
     * @param user The username of the user whose column is to be deleted.
     * @param n    The index of the column to be deleted.
     */
    @DeleteMapping("/coluna/{n}")
    void deleteCard(
            @PathVariable String user,
            @PathVariable int n) {
        columnService.deleteColumn(user, n);
    }
}
