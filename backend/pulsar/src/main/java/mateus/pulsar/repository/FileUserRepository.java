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
            connection = DriverManager.getConnection("jdbc:sqlite:user.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT, AUTHORITY TEXT)");

            //Insert default user if the table is empty
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                String defaultUsername = "test_user";
                String defaultPassword = "1504";
                String defaultAuthority = "ROLE_USER";
                statement.executeUpdate("INSERT INTO users (username, password, AUTHORITY) VALUES ('" + defaultUsername
                        + "', '" + defaultPassword + "', '" + defaultAuthority + "')");
            }
        } catch (SQLException e) {
            System.out.println("Connection to SQLite has failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user)  throws Exception {
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
}
