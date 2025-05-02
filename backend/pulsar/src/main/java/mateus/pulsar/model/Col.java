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

    /**
     * <p>
     * Construtor da classe Col.
     *
     * @param id    id da coluna. Caso o id ja exista, o construtor irá
     *              carregar os dados do arquivo JSON correspondente. Caso
     *              não exista, um novo arquivo será criado.
     * @param name  nome da coluna.
     * @param user  usuário dono da coluna.
     * @param order ordem da coluna.
     */
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

    /**
     * <p>
     * Retorna o id da coluna.
     *
     * @param card card a ser adicionado.
     * @return id da coluna.
     */
    public void addCard(Card card) {
        Card[] newCards = new Card[cards.length + 1];
        System.arraycopy(cards, 0, newCards, 0, cards.length);
        newCards[cards.length] = card;
        cards = newCards;
        this.save();
    }

    /**
     * <p>
     * Retorna o card na posição index.
     *
     * @param index índice do card a ser retornado.
     * @return Card na posição index.
     */
    public Card getCard(int index) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        return cards[index];
    }

    /**
     * <p>
     * Retorna todos os cards da coluna em formato JSON.
     *
     * @return String com todos os cards em formato JSON.
     */
    public String getCards() {
        StringBuilder cardList = new StringBuilder();
        for (Card card : cards) {
            cardList.append(card.getJson()).append("\n");
        }
        return cardList.toString();
    }

    /**
     * <p>
     * Remove um card da coluna.
     *
     * @param index índice do card a ser removido.
     */
    public void removeCard(int index) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        Card[] newCards = new Card[cards.length - 1];
        System.arraycopy(cards, 0, newCards, 0, index);
        System.arraycopy(cards, index + 1, newCards, index, cards.length - index - 1);
        cards = newCards;
        this.save();
    }

    /**
     * <p>
     * Atualiza o conteúdo de um card na coluna.
     *
     * @param index      índice do card a ser atualizado.
     * @param newContent novo conteúdo do card.
     */
    public void updateCard(int index, String newContent) {
        if (index < 0 || index >= cards.length) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        cards[index].setContent(newContent);
        this.save();
    }

    public int getCardCount() {
        return cards.length;
    }

    /**
     * <p>
     * Move o card de de uma posição para outra na mesma coluna.
     *
     * @param fromIndex índice do card a ser movido.
     * @param toIndex   índice para onde o card será movido.
     */
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
        this.save();
    }

    /**
     *
     * <p>
     * Save deve ser chamado sempre que houver uma alteração na coluna.
     * <p>
     * Responsacel por salvar o estado atual da coluna em um arquivo JSON.
     */
    private void save() {
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
