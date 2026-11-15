package ma.ens.pedagofeedback.dto;

import ma.ens.pedagofeedback.entities.Seance;

import java.time.LocalDate;
import java.util.List;

public class CalendrierJour {

    private final LocalDate date;
    private final boolean dansMois;
    private final boolean aujourdhui;
    private final List<Seance> seances;

    public CalendrierJour(LocalDate date, boolean dansMois, boolean aujourdhui, List<Seance> seances) {
        this.date = date;
        this.dansMois = dansMois;
        this.aujourdhui = aujourdhui;
        this.seances = seances;
    }

    public LocalDate getDate() { return date; }
    public boolean isDansMois() { return dansMois; }
    public boolean isAujourdhui() { return aujourdhui; }
    public List<Seance> getSeances() { return seances; }
}