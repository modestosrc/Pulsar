package mateus.pulsar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class Col {
    private String id;
    private String name;
    private int order;
    private Card[] cards;

    public Col(String name, int order) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.order = order;
        this.cards = new Card[0];
    }

    public void addCard(Card card) {
        Card[] newCards = new Card[cards.length + 1];
        System.arraycopy(cards, 0, newCards, 0, cards.length);
        newCards[cards.length] = card;
        cards = newCards;
    }

    public String getCards() {
        StringBuilder cardList = new StringBuilder();
        for (Card card : cards) {
            cardList.append(card.getContent()).append("\n");
        }
        return cardList.toString();
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("col_" + id + ".json")) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

