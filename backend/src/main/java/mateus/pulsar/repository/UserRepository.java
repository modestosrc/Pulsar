package mateus.pulsar.repository;

import org.springframework.security.core.userdetails.User;

public interface UserRepository {
    void save(User user) throws Exception;
    void updateUser(String username, User user) throws Exception;
    void deleteUser(String username) throws Exception;
    User loadUserByUsername(String username) throws Exception;
    void createDb() throws Exception;
}
