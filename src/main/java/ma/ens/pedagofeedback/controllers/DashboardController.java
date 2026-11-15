package ma.ens.pedagofeedback.controllers;

import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.services.EnseignantService;
import ma.ens.pedagofeedback.services.StatistiqueService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DashboardController {

    private final EnseignantService enseignantService;
    private final StatistiqueService statistiqueService;

    public DashboardController(EnseignantService enseignantService, StatistiqueService statistiqueService) {
        this.enseignantService = enseignantService;
        this.statistiqueService = statistiqueService;
    }

    /** Lien « Tableau de bord » du menu : chacun arrive sur le sien. */
    @GetMapping("/dashboard")
    public String monTableauDeBord(Authentication auth) {
        if (enseignantService.estAdmin(auth)) {
            return "redirect:/enseignants";
        }
        Enseignant courant = enseignantService.findByUsername(auth.getName());
        return "redirect:/dashboard/" + courant.getId();
    }

    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable Long id, Model model,
                            Authentication auth, RedirectAttributes ra) {
        try {
            boolean admin = enseignantService.estAdmin(auth);
            Enseignant courant = enseignantService.findByUsername(auth.getName());

            if (!admin && !courant.getId().equals(id)) {
                throw new RegleGestionException(
                        "Accès refusé : vous ne pouvez consulter que votre propre tableau de bord.");
            }
            model.addAttribute("enseignant", enseignantService.findById(id));
        } catch (RegleGestionException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/seances";
        }
        model.addAttribute("moyennes", statistiqueService.moyennesEnseignant(id));
        model.addAttribute("stats", statistiqueService.statsParSeance(id));
        model.addAttribute("commentaires", statistiqueService.commentaires(id));
        return "dashboard";
    }
}