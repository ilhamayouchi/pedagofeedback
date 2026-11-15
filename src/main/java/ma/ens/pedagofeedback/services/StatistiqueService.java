package ma.ens.pedagofeedback.services;



import ma.ens.pedagofeedback.dto.Moyennes;
import ma.ens.pedagofeedback.dto.SeanceStat;
import ma.ens.pedagofeedback.entities.Feedback;
import ma.ens.pedagofeedback.repositories.FeedbackRepository;
import ma.ens.pedagofeedback.repositories.SeanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatistiqueService {

    private final FeedbackRepository feedbackRepository;
    private final SeanceRepository seanceRepository;

    public StatistiqueService(FeedbackRepository feedbackRepository, SeanceRepository seanceRepository) {
        this.feedbackRepository = feedbackRepository;
        this.seanceRepository = seanceRepository;
    }

    /** Moyennes calculées séparément pour chaque critère, sur toutes les séances de l'enseignant. */
    public Moyennes moyennesEnseignant(Long enseignantId) {
        return feedbackRepository.moyennesParEnseignant(enseignantId);
    }

    /** Moyennes par critère et par séance. */
    public List<SeanceStat> statsParSeance(Long enseignantId) {
        return seanceRepository.statistiquesParEnseignant(enseignantId);
    }

    public List<Feedback> commentaires(Long enseignantId) {
        return feedbackRepository
                .findBySeance_Enseignant_IdAndCommentaireIsNotNullOrderByDateFeedbackDesc(enseignantId);
    }
}
