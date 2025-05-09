package mateus.pulsar.repository;

import org.springframework.security.core.userdetails.User;

public interface UserRepository {
    void save(User user) throws Exception;
    User loadUserByUsername(String username) throws Exception;
    void createDb() throws Exception;
}
