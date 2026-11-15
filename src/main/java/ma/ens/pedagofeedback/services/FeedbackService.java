package ma.ens.pedagofeedback.services;

import ma.ens.pedagofeedback.dto.FeedbackForm;
import ma.ens.pedagofeedback.entities.Feedback;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.repositories.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final SeanceService seanceService;

    public FeedbackService(FeedbackRepository feedbackRepository, SeanceService seanceService) {
        this.feedbackRepository = feedbackRepository;
        this.seanceService = seanceService;
    }

    /** Enregistre un feedback anonyme : aucune information sur l'étudiant n'est stockée. */
    public void enregistrer(String jeton, FeedbackForm form) {
        Seance seance = seanceService.findByJeton(jeton);

        if (!seance.isEvaluable()) {
            throw new RegleGestionException(
                    "Cette séance n'a pas encore eu lieu : elle ne peut pas être évaluée.");
        }

        Feedback feedback = new Feedback();
        feedback.setSeance(seance);
        feedback.setClarte(form.getClarte());
        feedback.setInteractivite(form.getInteractivite());
        feedback.setUtilite(form.getUtilite());

        String commentaire = form.getCommentaire();
        feedback.setCommentaire(commentaire == null || commentaire.isBlank() ? null : commentaire.trim());

        feedback.setDateFeedback(LocalDateTime.now());
        feedbackRepository.save(feedback);
    }
}