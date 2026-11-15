package ma.ens.pedagofeedback.services;

import ma.ens.pedagofeedback.dto.SeanceForm;
import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.repositories.SeanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final EnseignantService enseignantService;

    public SeanceService(SeanceRepository seanceRepository, EnseignantService enseignantService) {
        this.seanceRepository = seanceRepository;
        this.enseignantService = enseignantService;
    }

    /** L'admin voit toutes les séances, un enseignant voit seulement les siennes. */
    public List<Seance> findVisibles(Enseignant courant, boolean admin) {
        if (admin) {
            return seanceRepository.findAllByOrderByDateSeanceDescHeureSeanceDesc();
        }
        return seanceRepository.findByEnseignantIdOrderByDateSeanceDescHeureSeanceDesc(courant.getId());
    }

    /** Séances entre deux dates (pour le calendrier). */
    public List<Seance> findEntre(LocalDate debut, LocalDate fin, Enseignant courant, boolean admin) {
        List<Seance> seances = seanceRepository
                .findByDateSeanceBetweenOrderByDateSeanceAscHeureSeanceAsc(debut, fin);
        if (admin) {
            return seances;
        }
        return seances.stream()
                .filter(s -> s.getEnseignant().getId().equals(courant.getId()))
                .toList();
    }

    public Seance findById(Long id) {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new RegleGestionException("Séance introuvable (id : " + id + ")."));
    }

    /** Un enseignant ne peut ouvrir que ses propres séances. */
    public Seance findPourUtilisateur(Long id, Enseignant courant, boolean admin) {
        Seance seance = findById(id);
        if (!admin && !seance.getEnseignant().getId().equals(courant.getId())) {
            throw new RegleGestionException("Accès refusé à cette séance.");
        }
        return seance;
    }

    /** Utilisé par le lien du QR code. */
    public Seance findByJeton(String jeton) {
        return seanceRepository.findByJeton(jeton)
                .orElseThrow(() -> new RegleGestionException("Ce lien d'évaluation n'est pas valide."));
    }

    public Seance create(SeanceForm form, Long enseignantId) {
        Enseignant enseignant = enseignantService.findById(enseignantId);

        Seance seance = new Seance();
        seance.setEnseignant(enseignant);
        seance.setTitre(form.getTitre());
        seance.setMatiere(form.getMatiere());
        seance.setDateSeance(form.getDateSeance());
        seance.setHeureSeance(form.getHeureSeance());
        seance.setObjectifPedagogique(form.getObjectifPedagogique());
        seance.setMethodePedagogique(form.getMethodePedagogique());
        seance.setJeton(UUID.randomUUID().toString());
        return seanceRepository.save(seance);
    }
}