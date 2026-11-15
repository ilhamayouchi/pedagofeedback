package ma.ens.pedagofeedback.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedbackForm {

    @NotNull(message = "Veuillez noter la clarté")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer clarte;

    @NotNull(message = "Veuillez noter l'interactivité")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer interactivite;

    @NotNull(message = "Veuillez noter l'utilité")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer utilite;

    @Size(max = 2000, message = "Le commentaire ne doit pas dépasser 2000 caractères")
    private String commentaire;

    public Integer getClarte() { return clarte; }
    public void setClarte(Integer clarte) { this.clarte = clarte; }

    public Integer getInteractivite() { return interactivite; }
    public void setInteractivite(Integer interactivite) { this.interactivite = interactivite; }

    public Integer getUtilite() { return utilite; }
    public void setUtilite(Integer utilite) { this.utilite = utilite; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
