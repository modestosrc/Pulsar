package mateus.pulsar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.io.IOException;

public class Col {
    private int id;
    private String name;
    private String user;
    private int order;
    private Card[] cards;

    public Col(int id, String name, String user, int order) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.order = order;

        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        File file = new File(userDir, "col_" + id + ".json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Col col = gson.fromJson(reader, Col.class);
                this.name = col.name;
                this.user = col.user;
                this.cards = col.cards;
                this.order = col.order;
            } catch (IOException e) {
                e.printStackTrace();
                this.cards = new Card[0];
            }
        } else {
            this.cards = new Card[0];
        }
    }

    public void addCard(Card card) {
        Card[] newCards = new Card[cards.length + 1];
        System.arraycopy(cards, 0, newCards, 0, cards.length);
        newCards[cards.length] = card;
        cards = newCards;
    }

    public Card getCard(int index) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        return cards[index];
    }

    public String getCards() {
        StringBuilder cardList = new StringBuilder();
        for (Card card : cards) {
            cardList.append(card.getJson()).append("\n");
        }
        return cardList.toString();
    }

    public void removeCard(int index) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        Card[] newCards = new Card[cards.length - 1];
        System.arraycopy(cards, 0, newCards, 0, index);
        System.arraycopy(cards, index + 1, newCards, index, cards.length - index - 1);
        cards = newCards;
    }

    public void updateCard(int index, String newContent) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        cards[index].setContent(newContent);
    }

    public int getCardCount() {
        return cards.length;
    }

    public void moveCard(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= cards.length || toIndex < 0 || toIndex >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        if (fromIndex == toIndex) {
            return;
        }
        // Se o card for movido para o ultimo, nao ha necessidade de criar um
        // novo array, apenas remove o card e o adiciona no final
        if (toIndex == cards.length - 1) {
            Card card = cards[fromIndex];
            removeCard(fromIndex);
            addCard(card);
        } else {
            Card card = cards[fromIndex];
            Card[] novoArray = new Card[cards.length - 1];

            // Copia todos os elementos, exceto o que está em fromIndex
            int pos = 0;
            for (int i = 0; i < cards.length; i++) {
                if (i == fromIndex)
                    continue;
                novoArray[pos++] = cards[i];
            }

            // Agora criamos o array final, com espaço para inserir o card na posição
            // correta
            Card[] resultado = new Card[cards.length];
            for (int i = 0, j = 0; i < resultado.length; i++) {
                if (i == toIndex) {
                    resultado[i] = card;
                } else {
                    resultado[i] = novoArray[j++];
                }
            }

            cards = resultado;
        }
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File file = new File(userDir, "col_" + id + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
