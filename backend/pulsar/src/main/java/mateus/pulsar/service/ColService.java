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

    public void addCard(String user, int coln, Card card) {
        Col col = colRepository.load(user, coln);
        Card[] newCards = new Card[col.getCardCount() + 1];
        System.arraycopy(col.getCardList(), 0, newCards, 0, col.getCardCount());
        newCards[col.getCardCount()] = card;
        col.setCards(newCards);
        colRepository.save(col);
    }

    public void removeCard(String user, int coln, int index) {
        Col col = colRepository.load(user, coln);
        if (index < 0 || index >= col.getCardCount()) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        Card[] newCards = new Card[col.getCardCount() - 1];
        System.arraycopy(col.getCardList(), 0, newCards, 0, index);
        System.arraycopy(col.getCardList(), index + 1, newCards, index, col.getCardCount() - index - 1);
        col.setCards(newCards);
        colRepository.save(col);
    }

    public void moveCardInCol(String user, int coln, int fromIndex, int toIndex) {
        Col col = colRepository.load(user, coln);

        if (fromIndex < 0 || fromIndex >= col.getCardCount() || toIndex < 0 || toIndex >= col.getCardCount()) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        if (fromIndex == toIndex) {
            return;
        }
        // Se o card for movido para o ultimo, nao ha necessidade de criar um
        // novo array, apenas remove o card e o adiciona no final
        if (toIndex == col.getCardCount() - 1) {
            Card card = col.getCard(fromIndex);
            removeCard(user, coln, fromIndex);
            addCard(user, coln, card);
        } else {
            Card card = col.getCard(fromIndex);
            removeCard(user, coln, fromIndex);
            Card[] novoArray = new Card[col.getCardCount() - 1];

            // Copia todos os elementos, exceto o que está em fromIndex
            Card[] cardList = col.getCardList();
            int pos = 0;
            for (int i = 0; i < col.getCardCount(); i++) {
                if (i == fromIndex)
                    continue;
                novoArray[pos++] = cardList[i];
            }

            // Agora criamos o array final, com espaço para inserir o card na posição
            // correta
            Card[] resultado = new Card[cardList.length];
            for (int i = 0, j = 0; i < resultado.length; i++) {
                if (i == toIndex) {
                    resultado[i] = card;
                } else {
                    resultado[i] = novoArray[j++];
                }
            }

            cardList = resultado;
            col.setCards(cardList);
        }
        colRepository.save(col);
    }

    public void moveCardToCol(String user, int sourceColn, int targetColn, int fromIndex, int toIndex) {
        if (sourceColn == targetColn) {
            moveCardInCol(user, sourceColn, fromIndex, toIndex);
        }

        Col sourceCol = colRepository.load(user, sourceColn);
        Col targetCol = colRepository.load(user, targetColn);

        if (fromIndex < 0 || fromIndex >= sourceCol.getCardCount()) {
            throw new IndexOutOfBoundsException("Invalid card index in source column");
        }
        if (toIndex < 0 || toIndex >= targetCol.getCardCount()) {
            throw new IndexOutOfBoundsException("Invalid card index in target column");
        }

        Card card = sourceCol.getCard(fromIndex);
        removeCard(user, sourceColn, fromIndex);
        addCard(user, targetColn, card);
        moveCardInCol(user, targetColn, targetCol.getCardCount() - 1, toIndex);
    }

    public void setCardContent(String user, int coln, int index, String content) {
        Col col = colRepository.load(user, coln);
        if (index < 0 || index >= col.getCardCount()) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        col.setCard(index, new Card(content));
        colRepository.save(col);
    }
}
