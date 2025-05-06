package mateus.pulsar.repository;

import mateus.pulsar.model.Col;

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
            return new Col(colId, "Col " + colId, user, 0);
        }
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, Col.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
