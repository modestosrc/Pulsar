package mateus.pulsar.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository implements UserRepository {

    private Connection connection;
    private Statement statement;


    public FileUserRepository() {
        try {
            // Connect to SQLite database
            String url = "jdbc:sqlite:src/main/resources/database.db";
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            try {
                createDb();
            } catch (Exception e) {
                System.out.println("Error creating database");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Connection to SQLite has failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) throws Exception {
        try {
            String username = user.getUsername();
            String password = user.getPassword();
            String authority = user.getAuthorities().stream()
                    .findFirst()
                    .orElseThrow(() -> new Exception("Authority not found"))
                    .getAuthority();
            statement.executeUpdate("INSERT INTO users (username, password, AUTHORITY) VALUES ('" + username
                    + "', '" + password + "', '" + authority + "')");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao salvar usuário");
        }
    }

    @Override
    public User loadUserByUsername(String username) throws Exception {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            if (!rs.next()) {
                throw new Exception("Usuário não encontrado");
            }
            String password = rs.getString("password");
            String authority = rs.getString("AUTHORITY");
            return new User(username, password, List.of(new SimpleGrantedAuthority(authority)));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao carregar usuário");
        }
    }

    @Override
    public void createDb() throws Exception {
        try {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL, "
                    + "AUTHORITY TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erro ao criar banco de dados");
        }
    }
}
