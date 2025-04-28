package mateus.pulsar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.io.IOException;

public class Col {
    private int id;
    private String name;
    private int order;
    private Card[] cards;

    public Col(int id, String name, int order) {
        this.id = id;
        try (FileReader reader = new FileReader("col_" + id + ".json")) {
            Gson gson = new Gson();
            Col col = gson.fromJson(reader, Col.class);
            this.name = col.name;
            this.cards = col.cards;
            this.order = col.order;
        } catch (FileNotFoundException e) {
            this.cards = new Card[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
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
