package ma.ens.pedagofeedback.controllers;

import jakarta.validation.Valid;
import ma.ens.pedagofeedback.dto.FeedbackForm;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.services.FeedbackService;
import ma.ens.pedagofeedback.services.SeanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/evaluer/{jeton}")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final SeanceService seanceService;

    public FeedbackController(FeedbackService feedbackService, SeanceService seanceService) {
        this.feedbackService = feedbackService;
        this.seanceService = seanceService;
    }

    @GetMapping
    public String formulaire(@PathVariable String jeton, Model model) {
        try {
            Seance seance = seanceService.findByJeton(jeton);
            if (!seance.isEvaluable()) {
                model.addAttribute("message",
                        "Cette séance n'a pas encore eu lieu : elle ne peut pas être évaluée.");
                return "evaluation-erreur";
            }
            model.addAttribute("seance", seance);
            model.addAttribute("feedbackForm", new FeedbackForm());
            return "feedback-form";
        } catch (RegleGestionException e) {
            model.addAttribute("message", e.getMessage());
            return "evaluation-erreur";
        }
    }

    @PostMapping
    public String envoyer(@PathVariable String jeton,
                          @Valid @ModelAttribute("feedbackForm") FeedbackForm form,
                          BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("seance", seanceService.findByJeton(jeton));
                return "feedback-form";
            }
            feedbackService.enregistrer(jeton, form);
            return "redirect:/evaluer/" + jeton + "/merci";
        } catch (RegleGestionException e) {
            model.addAttribute("message", e.getMessage());
            return "evaluation-erreur";
        }
    }

    @GetMapping("/merci")
    public String merci() {
        return "merci";
    }
}