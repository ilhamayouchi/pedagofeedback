package ma.ens.pedagofeedback.dto;

import java.time.LocalDate;

public class SeanceStat {

    private final Long id;
    private final String titre;
    private final String matiere;
    private final LocalDate dateSeance;
    private final Double clarte;
    private final Double interactivite;
    private final Double utilite;
    private final Long nbFeedbacks;

    // Appelé par la requête @Query de SeanceRepository (select new ...SeanceStat(...))
    public SeanceStat(Long id, String titre, String matiere, LocalDate dateSeance,
                      Double clarte, Double interactivite, Double utilite, Long nbFeedbacks) {
        this.id = id;
        this.titre = titre;
        this.matiere = matiere;
        this.dateSeance = dateSeance;
        this.clarte = clarte;
        this.interactivite = interactivite;
        this.utilite = utilite;
        this.nbFeedbacks = nbFeedbacks;
    }

    public Long getId() { return id; }
    public String getTitre() { return titre; }
    public String getMatiere() { return matiere; }
    public LocalDate getDateSeance() { return dateSeance; }
    public Double getClarte() { return clarte; }
    public Double getInteractivite() { return interactivite; }
    public Double getUtilite() { return utilite; }
    public Long getNbFeedbacks() { return nbFeedbacks; }

    public int clartePct() { return pct(clarte); }
    public int interactivitePct() { return pct(interactivite); }
    public int utilitePct() { return pct(utilite); }

    private static int pct(Double note) {
        return note == null ? 0 : (int) Math.round(note * 20);
    }
}