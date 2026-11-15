package ma.ens.pedagofeedback.services;

import ma.ens.pedagofeedback.dto.EnseignantForm;
import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.repositories.EnseignantRepository;
import ma.ens.pedagofeedback.repositories.SeanceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final SeanceRepository seanceRepository;
    private final PasswordEncoder passwordEncoder;

    public EnseignantService(EnseignantRepository enseignantRepository,
                             SeanceRepository seanceRepository,
                             PasswordEncoder passwordEncoder) {
        this.enseignantRepository = enseignantRepository;
        this.seanceRepository = seanceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Enseignant> findAll() {
        return enseignantRepository.findAll(Sort.by("nom", "prenom"));
    }

    public Enseignant findById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new RegleGestionException("Enseignant introuvable (id : " + id + ")."));
    }

    /** Retrouve l'enseignant connecté à partir de son nom d'utilisateur. */
    public Enseignant findByUsername(String username) {
        return enseignantRepository.findByUsername(username)
                .orElseThrow(() -> new RegleGestionException("Compte introuvable : " + username));
    }

    public boolean estAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public void create(EnseignantForm form) {
        if (form.getMotDePasse() == null || form.getMotDePasse().isBlank()) {
            throw new RegleGestionException("Le mot de passe est obligatoire pour un nouvel enseignant.");
        }
        if (enseignantRepository.existsByUsername(form.getUsername())) {
            throw new RegleGestionException("Ce nom d'utilisateur est déjà utilisé.");
        }
        Enseignant enseignant = new Enseignant();
        copier(form, enseignant);
        enseignant.setMotDePasse(passwordEncoder.encode(form.getMotDePasse()));
        enseignant.setRole("ENSEIGNANT");
        enseignantRepository.save(enseignant);
    }

    @Transactional
    public void update(Long id, EnseignantForm form) {
        Enseignant enseignant = findById(id);

        Optional<Enseignant> autre = enseignantRepository.findByUsername(form.getUsername());
        if (autre.isPresent() && !autre.get().getId().equals(id)) {
            throw new RegleGestionException("Ce nom d'utilisateur est déjà utilisé.");
        }

        copier(form, enseignant);
        if (enseignant.getRole() == null) {
            enseignant.setRole("ENSEIGNANT");
        }
        // Le nom d'utilisateur du compte admin ne change jamais
        if ("ADMIN".equals(enseignant.getRole())) {
            enseignant.setUsername("admin");
        }
        if (form.getMotDePasse() != null && !form.getMotDePasse().isBlank()) {
            enseignant.setMotDePasse(passwordEncoder.encode(form.getMotDePasse()));
        }
    }

    public void delete(Long id) {
        Enseignant enseignant = findById(id);
        if ("ADMIN".equals(enseignant.getRole())) {
            throw new RegleGestionException("Le compte administrateur ne peut pas être supprimé.");
        }
        if (seanceRepository.existsByEnseignantId(id)) {
            throw new RegleGestionException(
                    "Impossible de supprimer cet enseignant : des séances lui sont associées.");
        }
        enseignantRepository.delete(enseignant);
    }

    /** Remplit le formulaire de modification (sans le mot de passe). */
    public EnseignantForm versForm(Enseignant enseignant) {
        EnseignantForm form = new EnseignantForm();
        form.setNom(enseignant.getNom());
        form.setPrenom(enseignant.getPrenom());
        form.setSpecialite(enseignant.getSpecialite());
        form.setEmail(enseignant.getEmail());
        form.setUsername(enseignant.getUsername());
        return form;
    }

    private void copier(EnseignantForm form, Enseignant enseignant) {
        enseignant.setNom(form.getNom());
        enseignant.setPrenom(form.getPrenom());
        enseignant.setSpecialite(form.getSpecialite());
        enseignant.setEmail(form.getEmail());
        enseignant.setUsername(form.getUsername());
    }
}