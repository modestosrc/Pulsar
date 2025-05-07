package mateus.pulsar.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private Connection connection;
    private Statement statement;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public UserDetailsServiceImpl() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:user.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT, AUTHORITY TEXT)");

            // Inser default user if the table is empty
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                String defaultUsername = "test_user";
                String defaultPassword = passwordEncoder.encode("1504");
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            if (!rs.next()) {
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
            String password = rs.getString("password");
            String authority = rs.getString("AUTHORITY");
            return new User(
                    username,
                    password,
                    List.of(new SimpleGrantedAuthority(authority)));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("Error loading user from database");
        }
    }

    public boolean isUserValid(String username, String password) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            if (!rs.next()) {
                return false;
            }
            String encodedPassword = rs.getString("password");
            return passwordEncoder.matches(password, encodedPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean doesUserExist(String username) {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
