package mateus.pulsar.repository;

import mateus.pulsar.model.Column;

public interface ColumnRepository {
    void save(Column column);
    Column load(String user, int colId);
    Column[] loadAll(String user);
    void delete(Column column);
}
