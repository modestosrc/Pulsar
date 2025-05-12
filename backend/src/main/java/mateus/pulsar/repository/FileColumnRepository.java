package mateus.pulsar.repository;

import mateus.pulsar.model.Column;
import mateus.pulsar.model.Card;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Repository
public class FileColumnRepository implements ColumnRepository {

    @Override
    public void save(Column column) {
        File userDir = new File(column.getUser());
        if (!userDir.exists()) {
            userDir.mkdirs();
            System.out.println("Diretório criado: " + userDir.getAbsolutePath());
        }
        File file = new File(userDir, "col_" + column.getId() + ".json");
        if (file.exists()) {
            return;
        }
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(column);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Column load(String user, int columnId) {
        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File file = new File(userDir, "col_" + columnId + ".json");
        if (!file.exists()) {
            return new Column(columnId, "Col " + columnId, user, new Card[0]);
        }
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Column column = gson.fromJson(reader, Column.class);
            return column;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Column[] loadAll(String user) {
        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
            return new Column[0];
        }
        File[] files = userDir.listFiles((_, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            return new Column[0];
        }
        Column[] columns = new Column[files.length];
        for (int i = 0; i < files.length; i++) {
            try (FileReader reader = new FileReader(files[i])) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                columns[i] = gson.fromJson(reader, Column.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return columns;
    }

    @Override
    public void delete(Column column) {
        File userDir = new File(column.getUser());
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File file = new File(userDir, "col_" + column.getId() + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}
