package mateus.pulsar.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InMemoryUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("test_user")) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        // HACK: 
        // - Isso não deve ser feito em produção.
        return new User(
            "test_user",
            "{noop}1504", // {noop} diz ao Spring que a senha não está codificada
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
