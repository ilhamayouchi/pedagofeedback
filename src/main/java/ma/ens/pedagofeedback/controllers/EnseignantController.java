package ma.ens.pedagofeedback.controllers;

import jakarta.validation.Valid;
import ma.ens.pedagofeedback.dto.EnseignantForm;
import ma.ens.pedagofeedback.exception.RegleGestionException;
import ma.ens.pedagofeedback.services.EnseignantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enseignants")
public class EnseignantController {

    private final EnseignantService enseignantService;

    public EnseignantController(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("enseignants", enseignantService.findAll());
        return "enseignants";
    }

    @GetMapping("/nouveau")
    public String nouveau(Model model) {
        model.addAttribute("enseignantForm", new EnseignantForm());
        preparerFormulaire(model, "Nouvel enseignant", "/enseignants/nouveau", true);
        return "enseignant-form";
    }

    @PostMapping("/nouveau")
    public String creer(@Valid @ModelAttribute("enseignantForm") EnseignantForm form,
                        BindingResult result, Model model, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            try {
                enseignantService.create(form);
                ra.addFlashAttribute("succes", "Enseignant ajouté avec succès.");
                return "redirect:/enseignants";
            } catch (RegleGestionException e) {
                model.addAttribute("erreur", e.getMessage());
            }
        }
        preparerFormulaire(model, "Nouvel enseignant", "/enseignants/nouveau", true);
        return "enseignant-form";
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("enseignantForm",
                    enseignantService.versForm(enseignantService.findById(id)));
        } catch (RegleGestionException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/enseignants";
        }
        preparerFormulaire(model, "Modifier un enseignant", "/enseignants/" + id + "/modifier", false);
        return "enseignant-form";
    }

    @PostMapping("/{id}/modifier")
    public String mettreAJour(@PathVariable Long id,
                              @Valid @ModelAttribute("enseignantForm") EnseignantForm form,
                              BindingResult result, Model model, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            try {
                enseignantService.update(id, form);
                ra.addFlashAttribute("succes", "Enseignant modifié avec succès.");
                return "redirect:/enseignants";
            } catch (RegleGestionException e) {
                model.addAttribute("erreur", e.getMessage());
            }
        }
        preparerFormulaire(model, "Modifier un enseignant", "/enseignants/" + id + "/modifier", false);
        return "enseignant-form";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            enseignantService.delete(id);
            ra.addFlashAttribute("succes", "Enseignant supprimé.");
        } catch (RegleGestionException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/enseignants";
    }

    private void preparerFormulaire(Model model, String titre, String action, boolean nouveau) {
        model.addAttribute("titre", titre);
        model.addAttribute("action", action);
        model.addAttribute("nouveau", nouveau);
    }
}