package mateus.pulsar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    }

    public Card getCard(int index) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        return cards[index];
    }

    public Card[] getCardList() {
        if (cards == null) {
            return new Card[0];
        }
        return cards;
    }

    public String getCardAsJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public void setCard(int index, Card card) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        cards[index] = card;
    }

    public String getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public int getCardCount() {
        return cards.length;
    }
}
