package ma.ens.pedagofeedback.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enseignant")
public class Enseignant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String specialite;

    @Column(nullable = false, length = 150)
    private String email;

    // Identifiants de connexion
    @Column(unique = true, length = 50)
    private String username;

    @Column(name = "mot_de_passe", length = 100)
    private String motDePasse;

    // "ADMIN" ou "ENSEIGNANT"
    @Column(length = 20)
    private String role = "ENSEIGNANT";

    @OneToMany(mappedBy = "enseignant")
    private List<Seance> seances = new ArrayList<>();

    public Enseignant() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Seance> getSeances() { return seances; }
    public void setSeances(List<Seance> seances) { this.seances = seances; }
}