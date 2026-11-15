package ma.ens.pedagofeedback.controllers;

import jakarta.validation.Valid;
import ma.ens.pedagofeedback.dto.SeanceForm;
import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.services.EnseignantService;
import ma.ens.pedagofeedback.services.QrCodeService;
import ma.ens.pedagofeedback.services.SeanceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/seances")
public class SeanceController {

    private final SeanceService seanceService;
    private final EnseignantService enseignantService;
    private final QrCodeService qrCodeService;
    private final String baseUrl;

    public SeanceController(SeanceService seanceService,
                            EnseignantService enseignantService,
                            QrCodeService qrCodeService,
                            @Value("${app.base-url}") String baseUrl) {
        this.seanceService = seanceService;
        this.enseignantService = enseignantService;
        this.qrCodeService = qrCodeService;
        this.baseUrl = baseUrl;
    }

    @GetMapping
    public String liste(Model model, Authentication auth) {
        boolean admin = enseignantService.estAdmin(auth);
        Enseignant courant = enseignantService.findByUsername(auth.getName());
        model.addAttribute("seances", seanceService.findVisibles(courant, admin));
        return "seances";
    }

    @GetMapping("/nouvelle")
    public String nouvelle(Model model, Authentication auth) {
        model.addAttribute("seanceForm", new SeanceForm());
        preparerFormulaire(model, enseignantService.estAdmin(auth));
        return "seance-form";
    }

    @PostMapping("/nouvelle")
    public String creer(@Valid @ModelAttribute("seanceForm") SeanceForm form,
                        BindingResult result, Model model,
                        Authentication auth, RedirectAttributes ra) {
        boolean admin = enseignantService.estAdmin(auth);

        if (admin && form.getEnseignantId() == null) {
            result.rejectValue("enseignantId", "obligatoire", "Veuillez choisir un enseignant");
        }
        if (result.hasErrors()) {
            preparerFormulaire(model, admin);
            return "seance-form";
        }

        // Un enseignant planifie toujours ses propres séances
        Long enseignantId = admin
                ? form.getEnseignantId()
                : enseignantService.findByUsername(auth.getName()).getId();

        try {
            seanceService.create(form, enseignantId);
            ra.addFlashAttribute("succes", "Séance enregistrée avec succès.");
        } catch (RegleGestionException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/seances";
    }

    /** Page qui affiche le QR code à projeter en classe. */
    @GetMapping("/{id}/qr")
    public String qr(@PathVariable Long id, Model model, Authentication auth, RedirectAttributes ra) {
        try {
            Seance seance = trouverSeance(id, auth);
            model.addAttribute("seance", seance);
            model.addAttribute("lien", lienEvaluation(seance));
            return "qr";
        } catch (RegleGestionException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/seances";
        }
    }

    /** L'image PNG du QR code. */
    @GetMapping(value = "/{id}/qr.png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] qrImage(@PathVariable Long id, Authentication auth) {
        Seance seance = trouverSeance(id, auth);
        return qrCodeService.generer(lienEvaluation(seance));
    }

    private Seance trouverSeance(Long id, Authentication auth) {
        boolean admin = enseignantService.estAdmin(auth);
        Enseignant courant = enseignantService.findByUsername(auth.getName());
        return seanceService.findPourUtilisateur(id, courant, admin);
    }

    private String lienEvaluation(Seance seance) {
        String base = baseUrl.replaceAll("/+$", "");
        return base + "/evaluer/" + seance.getJeton();
    }

    private void preparerFormulaire(Model model, boolean admin) {
        model.addAttribute("estAdmin", admin);
        model.addAttribute("enseignants", admin ? enseignantService.findAll() : List.of());
    }
}