package ma.ens.pedagofeedback.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seance_id", nullable = false)
    private Seance seance;

    @NotNull @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer clarte;

    @NotNull @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer interactivite;

    @NotNull @Min(1) @Max(5)
    @Column(nullable = false)
    private Integer utilite;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_feedback", nullable = false)
    private LocalDateTime dateFeedback;

    public Feedback() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Seance getSeance() { return seance; }
    public void setSeance(Seance seance) { this.seance = seance; }

    public Integer getClarte() { return clarte; }
    public void setClarte(Integer clarte) { this.clarte = clarte; }

    public Integer getInteractivite() { return interactivite; }
    public void setInteractivite(Integer interactivite) { this.interactivite = interactivite; }

    public Integer getUtilite() { return utilite; }
    public void setUtilite(Integer utilite) { this.utilite = utilite; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDateFeedback() { return dateFeedback; }
    public void setDateFeedback(LocalDateTime dateFeedback) { this.dateFeedback = dateFeedback; }
}