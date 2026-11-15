package ma.ens.pedagofeedback.controllers;

import ma.ens.pedagofeedback.dto.CalendrierJour;
import ma.ens.pedagofeedback.entities.Enseignant;
import ma.ens.pedagofeedback.entities.Seance;
import ma.ens.pedagofeedback.services.EnseignantService;
import ma.ens.pedagofeedback.services.SeanceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CalendrierController {

    private final SeanceService seanceService;
    private final EnseignantService enseignantService;

    public CalendrierController(SeanceService seanceService, EnseignantService enseignantService) {
        this.seanceService = seanceService;
        this.enseignantService = enseignantService;
    }

    @GetMapping("/calendrier")
    public String calendrier(@RequestParam(required = false) String mois,
                             Model model, Authentication auth) {

        // Mois demandé (format 2026-11), sinon le mois actuel
        YearMonth moisAffiche;
        try {
            moisAffiche = (mois == null) ? YearMonth.now() : YearMonth.parse(mois);
        } catch (Exception e) {
            moisAffiche = YearMonth.now();
        }

        boolean admin = enseignantService.estAdmin(auth);
        Enseignant courant = enseignantService.findByUsername(auth.getName());

        // Le calendrier commence un lundi et finit un dimanche
        LocalDate debut = moisAffiche.atDay(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fin = moisAffiche.atEndOfMonth().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<Seance> seances = seanceService.findEntre(debut, fin, courant, admin);
        Map<LocalDate, List<Seance>> parJour = seances.stream()
                .collect(Collectors.groupingBy(Seance::getDateSeance));

        // Construction des semaines (7 jours par ligne)
        List<List<CalendrierJour>> semaines = new ArrayList<>();
        LocalDate jour = debut;
        while (!jour.isAfter(fin)) {
            List<CalendrierJour> semaine = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                semaine.add(new CalendrierJour(
                        jour,
                        YearMonth.from(jour).equals(moisAffiche),
                        jour.equals(LocalDate.now()),
                        parJour.getOrDefault(jour, List.of())));
                jour = jour.plusDays(1);
            }
            semaines.add(semaine);
        }

        String titre = moisAffiche.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH)
                + " " + moisAffiche.getYear();

        model.addAttribute("semaines", semaines);
        model.addAttribute("titreMois", titre);
        model.addAttribute("precedent", moisAffiche.minusMonths(1).toString());
        model.addAttribute("suivant", moisAffiche.plusMonths(1).toString());
        model.addAttribute("moisActuel", YearMonth.now().toString());
        return "calendrier";
    }
}