package mateus.pulsar.service;

import mateus.pulsar.model.Col;
import mateus.pulsar.model.Card;
import mateus.pulsar.repository.ColRepository;

import org.springframework.stereotype.Service;

@Service
public class ColService {
    private final ColRepository colRepository;

    public ColService(ColRepository colRepository) {
        this.colRepository = colRepository;
    }

    public Col getCol(String user, int coln) {
        return colRepository.load(user, coln);
    }

    public Col[] getAllCols(String user) {
        return colRepository.loadAll(user);
    }

    public Col createCol(String user) {
        Col col = new Col(0, "Col " + 0, user, new Card[0]);
        colRepository.save(col);
        return col;
    }

    public Col setCol(String user, int coln, Col col) {
        colRepository.save(col);
        return col;
    }

    public void deleteCol(String user, int coln) {
    }
}
