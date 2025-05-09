package mateus.pulsar.repository;

import mateus.pulsar.model.Col;
import mateus.pulsar.model.Card;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Repository
public class FileColRepository implements ColRepository {

    @Override
    public void save(Col col) {
        File userDir = new File(col.getUser());
        if (!userDir.exists()) {
            userDir.mkdirs();
            System.out.println("Diretório criado: " + userDir.getAbsolutePath());
        }
        File file = new File(userDir, "col_" + col.getId() + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(col);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Col load(String user, int colId) {
        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File file = new File(userDir, "col_" + colId + ".json");
        if (!file.exists()) {
            return new Col(colId, "Col " + colId, user, new Card[0]);
        }
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Col col = gson.fromJson(reader, Col.class);
            return col;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Col[] loadAll(String user) {
        File userDir = new File(user);
        if (!userDir.exists()) {
            userDir.mkdirs();
            return new Col[0];
        }
        File[] files = userDir.listFiles((_, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            return new Col[0];
        }
        Col[] cols = new Col[files.length];
        for (int i = 0; i < files.length; i++) {
            try (FileReader reader = new FileReader(files[i])) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                cols[i] = gson.fromJson(reader, Col.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cols;
    }

    @Override
    public void delete(Col col) {
        File userDir = new File(col.getUser());
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        File file = new File(userDir, "col_" + col.getId() + ".json");
        if (file.exists()) {
            file.delete();
        }
    }
}
