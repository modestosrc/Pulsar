package mateus.pulsar.repository;

import mateus.pulsar.model.Col;

public interface ColRepository {
    void save(Col col);
    Col load(String user, int colId);
}
