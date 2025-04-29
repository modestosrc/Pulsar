package mateus.pulsar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Card {
    private String id;
    private String content;

    public Card(String content) {
        this.id = java.util.UUID.randomUUID().toString();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
