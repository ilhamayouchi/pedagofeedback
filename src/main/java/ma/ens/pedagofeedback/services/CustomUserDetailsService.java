package ma.ens.pedagofeedback.services;

import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.repositories.EnseignantRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EnseignantRepository enseignantRepository;

    public CustomUserDetailsService(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Enseignant enseignant = enseignantRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        String role = (enseignant.getRole() == null) ? "ENSEIGNANT" : enseignant.getRole();

        return User.withUsername(enseignant.getUsername())
                .password(enseignant.getMotDePasse())
                .roles(role)
                .build();
    }
}