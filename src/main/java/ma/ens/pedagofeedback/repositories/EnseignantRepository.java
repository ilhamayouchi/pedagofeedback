package ma.ens.pedagofeedback.repositories;

import ma.ens.pedagofeedback.entities.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {

    Optional<Enseignant> findByUsername(String username);

    boolean existsByUsername(String username);
}