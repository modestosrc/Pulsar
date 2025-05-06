package mateus.pulsar.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private BCryptPasswordEncoder passwordEncoder;
    private Connection connection;
    private Statement statement;

    public UserDetailsServiceImpl() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:user.db");
            statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (username TEXT, password TEXT)");

            passwordEncoder = new BCryptPasswordEncoder();

            // Inser default user if the table is empty
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                String defaultUsername = "test_user";
                String defaultPassword = passwordEncoder.encode("1504");
                statement.executeUpdate("INSERT INTO users (username, password) VALUES ('" + defaultUsername + "', '" + defaultPassword + "')");
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
            return new User(
                    username,
                    password,
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
        } catch (SQLException e) {
            System.out.println("Error loading user from database.");
            e.printStackTrace();
            throw new UsernameNotFoundException("Error loading user from database");
        }
    }

    public boolean isUserValid(String username, String password) {
        UserDetails user = loadUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean doesUserExist(String username) {
        try {
            loadUserByUsername(username);
            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
