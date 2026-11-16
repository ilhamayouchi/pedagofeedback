# PedagoFeedback

Application web d'évaluation des séances d'enseignement par les étudiants.
Les enseignants planifient leurs séances, partagent un **QR code** ou un **lien** aux étudiants, et consultent les résultats agrégés dans un tableau de bord.

> Mini-projet Web (JEE) : Spring Boot + Thymeleaf
> Réalisé par : **ilham ayouchi**, encadré par : **Pr.Mohammed Laghgar**

---

## Sommaire

- [Problématique](#problématique)
- [Objectif](#objectif)
- [Fonctionnalités](#fonctionnalités)
- [Règles de gestion](#règles-de-gestion)
- [Les diagrammes UML](#les-diagrammes-uml)
- [Modèle de données](#modèle-de-données)
- [Technologies](#technologies)
- [Structure du projet](#structure-du-projet)
- [Démonstration](#démonstration)

---

## Problématique

L'amélioration des pratiques d'enseignement nécessite la collecte régulière des perceptions et des suggestions des étudiants. Sans système de feedback, les enseignants ont du mal à identifier les points forts de leurs séances et les aspects nécessitant une amélioration.

## Objectif

Concevoir une application web permettant aux étudiants d'évaluer les séances pédagogiques de façon anonyme, et aux enseignants d'analyser les résultats agrégés.

---

## Fonctionnalités

### Demandées par le cahier des charges

- **Gérer les enseignants** (réservé au compte administrateur).
- **Planifier et enregistrer les séances**, chaque séance étant associée à un enseignant.
- **Partager le formulaire d'évaluation** par **QR code** ou par **lien**, avec un jeton unique propre à chaque séance.
- **Évaluer une séance** sur trois critères : clarté, interactivité, utilité (notes de 1 à 5).
- **Laisser un commentaire** pédagogique (facultatif).
- **Calculer les moyennes** par critère et par séance.
- **Tableau de bord** par enseignant, avec les résultats agrégés et des graphiques en barres des moyennes par critère.

### Extensions ajoutées

- **Connexion des enseignants** (Spring Security) : les étudiants n'ont jamais de compte, ce qui préserve l'anonymat.
- **Gestion des rôles** : `ADMIN` et `ENSEIGNANT`. Un enseignant ne voit que ses propres séances et son propre tableau de bord.
- **Heure de la séance** en plus de la date : une séance devient évaluable à partir de sa date **et** de son heure.
- **Calendrier mensuel** des séances, avec la date et l'heure.
- **QR code** généré pour chaque séance (bibliothèque ZXing).

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

<img width="614" height="413" alt="image" src="https://github.com/user-attachments/assets/a48d1084-eaf8-4ac0-ac8a-93b9a1e7aa61" />

### Diagramme de classes

<img width="758" height="247" alt="image" src="https://github.com/user-attachments/assets/279c036c-4436-48ec-8d5e-90728224e159" />

---

## Modèle de données

Relations :

- Un enseignant assure plusieurs séances.
- Une séance peut recevoir plusieurs feedbacks anonymes.

| Table | Rôle |
|---|---|
| `enseignant` | Les comptes des enseignants et de l'administrateur |
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
  `username` VARCHAR(50) DEFAULT NULL,       -- identifiant de connexion
  `mot_de_passe` VARCHAR(100) DEFAULT NULL,  -- haché avec BCrypt
  `role` VARCHAR(20) DEFAULT NULL,           -- ADMIN ou ENSEIGNANT
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
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
  UNIQUE KEY (`jeton`),
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

La table `feedback` ne contient **aucune colonne** permettant d'identifier un étudiant.

---

## Technologies

- **Langage** : Java 17
- **Framework** : Spring Boot 3 (Spring MVC, Spring Data JPA, Bean Validation)
- **Sécurité** : Spring Security, mots de passe hachés avec BCrypt
- **Moteur de templates** : Thymeleaf (avec l'extension Spring Security)
- **Base de données** : MySQL (XAMPP et phpMyAdmin en local)
- **QR code** : ZXing
- **Conteneurisation** : Docker (pour le déploiement)
- **Outils de développement** : IntelliJ IDEA, Maven, Git et GitHub

## Structure du projet

```
pedagofeedback/
├── pom.xml
└── src/main/
    ├── java/ma/ens/pedagofeedback/
    │   ├── PedagoFeedbackApplication.java
    │   ├── config/          SecurityConfig, DatabaseInitializer
    │   ├── controllers/     Login, Home, Enseignant, Seance, Feedback, Dashboard, Calendrier
    │   ├── dto/             SeanceForm, FeedbackForm, EnseignantForm, Moyennes, SeanceStat, CalendrierJour
    │   ├── entities/        Enseignant, Seance, Feedback
    │   ├── exception/       RegleGestionException
    │   ├── repositories/    EnseignantRepository, SeanceRepository, FeedbackRepository
    │   └── services/        Enseignant, Seance, Feedback, Statistique, QrCode, CustomUserDetails
    └── resources/
        ├── application.properties
        ├── static/style.css
        └── templates/       pages Thymeleaf (login, séances, calendrier, QR code, tableau de bord...)
```

---

## Utilisation

1. L'**administrateur** se connecte et crée les comptes des enseignants (menu Enseignants).
2. Un **enseignant** se connecte et planifie une séance (titre, matière, date, heure, méthode, objectif).
3. Il retrouve la séance dans la liste et le **calendrier**, puis clique sur **QR code**.
4. Il projette le QR code (ou partage le lien) **après la séance**.
5. Les **étudiants** le scannent avec leur téléphone, sans compte, et remplissent le formulaire anonyme.
6. L'enseignant consulte son **tableau de bord** : moyennes par critère, résultats par séance, commentaires anonymes.

## Démonstration

https://github.com/user-attachments/assets/6f76005d-25c2-441e-b250-44c244789c01
