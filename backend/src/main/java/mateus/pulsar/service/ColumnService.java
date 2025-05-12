package mateus.pulsar.service;

import mateus.pulsar.model.Column;
import mateus.pulsar.model.Card;
import mateus.pulsar.repository.ColumnRepository;

import org.springframework.stereotype.Service;

@Service
public class ColumnService {
    private final ColumnRepository columnRepository;

    public ColumnService(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    public Column getColumn(String user, int columnN) {
        return columnRepository.load(user, columnN);
    }

    public Column[] getAllColumns(String user) {
        return columnRepository.loadAll(user);
    }

    public Column createColumn(String user, int columnN) {
        Column column = new Column(columnN, "Col " + columnN, user, new Card[0]);
        columnRepository.save(column);
        return column;
    }

    public Column setColumn(String user, int columnN, Column column) {
        columnRepository.save(column);
        return column;
    }

    public void deleteColumn(String user, int columnN) {
    }
}
