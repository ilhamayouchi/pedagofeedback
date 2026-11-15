package ma.ens.pedagofeedback.config;

import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.repositories.EnseignantRepository;
import ma.ens.pedagofeedback.repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner init(EnseignantRepository enseignantRepository,
                           SeanceRepository seanceRepository,
                           PasswordEncoder encoder,
                           @Value("${app.admin-password}") String motDePasseAdmin) {
        return args -> {
            // Compte administrateur, créé une seule fois
            if (!enseignantRepository.existsByUsername("admin")) {
                Enseignant admin = new Enseignant();
                admin.setNom("Administrateur");
                admin.setPrenom("Compte");
                admin.setSpecialite("Gestion");
                admin.setEmail("admin@pedagofeedback.local");
                admin.setUsername("admin");
                admin.setMotDePasse(encoder.encode(motDePasseAdmin));
                admin.setRole("ADMIN");
                enseignantRepository.save(admin);
            }

            // Donne un code secret aux séances qui n'en ont pas encore
            for (Seance seance : seanceRepository.findAll()) {
                if (seance.getJeton() == null) {
                    seance.setJeton(UUID.randomUUID().toString());
                    seanceRepository.save(seance);
                }
            }
        };
    }
}