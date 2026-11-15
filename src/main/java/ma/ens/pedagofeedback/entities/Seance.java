package ma.ens.pedagofeedback.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seance")
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(nullable = false, length = 100)
    private String matiere;

    @Column(name = "date_seance", nullable = false)
    private LocalDate dateSeance;

    @Column(name = "heure_seance")
    private LocalTime heureSeance;

    @Column(name = "objectif_pedagogique", nullable = false, columnDefinition = "TEXT")
    private String objectifPedagogique;

    @Column(name = "methode_pedagogique", nullable = false, length = 100)
    private String methodePedagogique;

    // Code secret utilisé dans le lien du QR code
    @Column(unique = true, length = 40)
    private String jeton;

    @OneToMany(mappedBy = "seance")
    private List<Feedback> feedbacks = new ArrayList<>();

    public Seance() {
    }

    /** Date et heure de la séance (minuit si l'heure n'est pas renseignée). */
    public LocalDateTime getDateHeure() {
        if (dateSeance == null) {
            return null;
        }
        LocalTime heure = (heureSeance == null) ? LocalTime.MIDNIGHT : heureSeance;
        return LocalDateTime.of(dateSeance, heure);
    }

    /** Règle de gestion : pas d'évaluation avant la date et l'heure de la séance. */
    public boolean isEvaluable() {
        LocalDateTime debut = getDateHeure();
        return debut != null && !debut.isAfter(LocalDateTime.now());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }

    public LocalDate getDateSeance() { return dateSeance; }
    public void setDateSeance(LocalDate dateSeance) { this.dateSeance = dateSeance; }

    public LocalTime getHeureSeance() { return heureSeance; }
    public void setHeureSeance(LocalTime heureSeance) { this.heureSeance = heureSeance; }

    public String getObjectifPedagogique() { return objectifPedagogique; }
    public void setObjectifPedagogique(String objectifPedagogique) { this.objectifPedagogique = objectifPedagogique; }

    public String getMethodePedagogique() { return methodePedagogique; }
    public void setMethodePedagogique(String methodePedagogique) { this.methodePedagogique = methodePedagogique; }

    public String getJeton() { return jeton; }
    public void setJeton(String jeton) { this.jeton = jeton; }

    public List<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
}