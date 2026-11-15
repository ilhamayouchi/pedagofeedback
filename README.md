# PedagoFeedback

Application web d'évaluation des séances d'enseignement par les étudiants.  
Les enseignants planifient leurs séances, partagent un **QR code** ou un **lien** aux étudiants, et consultent les résultats agrégés dans un tableau de bord.


---

## Problématique

L'amélioration des pratiques d'enseignement nécessite la collecte régulière des perceptions et des suggestions des étudiants. Sans système de feedback, les enseignants ont du mal à identifier les points forts de leurs séances et les aspects nécessitant une amélioration.

## Objectif

Concevoir une application web permettant aux étudiants d'évaluer les séances pédagogiques de façon anonyme, et aux enseignants d'analyser les résultats agrégés.

---

## Fonctionnalités

- **Gérer les enseignants** (réservé au compte administrateur).
- **Planifier et enregistrer les séances**, chaque séance étant associée à un enseignant.
- **Partager le formulaire d'évaluation** par **QR code** ou par **lien** unique (jeton propre à chaque séance).
- **Évaluer une séance** sur trois critères : clarté, interactivité, utilité (notes de 1 à 5).
- **Laisser un commentaire** pédagogique facultatif.
- **Calculer les moyennes** par critère et par séance.
- **Tableau de bord** présentant les résultats agrégés.
- **Gestion des rôles** : `ADMIN` et `ENSEIGNANT`.

## Règles de gestion

- Chaque séance est associée à un enseignant.
- Chaque critère d'évaluation reçoit une note comprise entre 1 et 5.
- Les feedbacks sont **anonymes** : aucun nom, adresse électronique ou identifiant d'étudiant n'est enregistré.
- Les tableaux de bord présentent principalement des résultats agrégés.
- Les moyennes sont calculées séparément pour chaque critère.
- Une séance ne peut pas être évaluée avant sa date de réalisation.
- Sans mécanisme d'identification, le système ne garantit pas qu'un étudiant ne réponde qu'une seule fois.

---
## Les diagrammes UML

### Diagramme de cas d'utilisation

<!-- Remplacer par votre image : ![Cas d'utilisation](docs/use-case.png) -->

### Diagramme de classes

<!-- Remplacer par votre image : ![Diagramme de classes](docs/class-diagram.png) -->

## Modèle de données

Relations :

- Un enseignant assure plusieurs séances.
- Une séance peut recevoir plusieurs feedbacks anonymes.

| Table | Rôle |
|---|---|
| `enseignant` | Comptes des enseignants et de l'administrateur |
| `seance` | Séances d'enseignement planifiées |
| `feedback` | Évaluations anonymes des étudiants |

### Tables SQL

Les tables sont créées automatiquement par Hibernate (`spring.jpa.hibernate.ddl-auto=update`). Voici leur structure :

```sql
CREATE TABLE `enseignant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nom` VARCHAR(100) NOT NULL,
  `prenom` VARCHAR(100) NOT NULL,
  `specialite` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `username` VARCHAR(50) DEFAULT NULL,
  `mot_de_passe` VARCHAR(100) DEFAULT NULL,  -- haché avec BCrypt
  `role` VARCHAR(20) DEFAULT NULL,           -- ADMIN ou ENSEIGNANT
  PRIMARY KEY (`id`)
);

CREATE TABLE `seance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(150) NOT NULL,
  `matiere` VARCHAR(100) NOT NULL,
  `date_seance` DATE NOT NULL,
  `heure_seance` TIME(6) DEFAULT NULL,
  `objectif_pedagogique` TEXT NOT NULL,
  `methode_pedagogique` VARCHAR(100) NOT NULL,
  `jeton` VARCHAR(40) DEFAULT NULL,          -- utilisé dans le lien et le QR code
  `enseignant_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`enseignant_id`) REFERENCES `enseignant`(`id`)
);

CREATE TABLE `feedback` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `clarte` INT NOT NULL,
  `interactivite` INT NOT NULL,
  `utilite` INT NOT NULL,
  `commentaire` TEXT DEFAULT NULL,
  `date_feedback` DATETIME(6) NOT NULL,
  `seance_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`seance_id`) REFERENCES `seance`(`id`)
);
```


## Technologies

- **Langage** : Java
- **Framework** : Spring Boot
- **Sécurité** : Spring Security, mots de passe hachés avec BCrypt
- **Moteur de templates** : Thymeleaf
- **Base de données** : MySQL (XAMPP et phpMyAdmin en local)
- **Outils de développement** : IntelliJ IDEA, Git et GitHub

## Guide d'utilisation :
