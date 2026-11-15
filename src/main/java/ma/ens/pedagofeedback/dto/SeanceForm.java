package ma.ens.pedagofeedback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class SeanceForm {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La matière est obligatoire")
    private String matiere;

    @NotNull(message = "La date de la séance est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateSeance;

    @NotNull(message = "L'heure de la séance est obligatoire")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime heureSeance;

    @NotBlank(message = "L'objectif pédagogique est obligatoire")
    private String objectifPedagogique;

    @NotBlank(message = "La méthode pédagogique est obligatoire")
    private String methodePedagogique;

    // Choisi seulement par l'admin. Un enseignant planifie toujours ses propres séances.
    private Long enseignantId;

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

    public Long getEnseignantId() { return enseignantId; }
    public void setEnseignantId(Long enseignantId) { this.enseignantId = enseignantId; }
}