package mateus.pulsar.model;

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

    public void setContent(String content) {
        this.content = content;
    }
}
