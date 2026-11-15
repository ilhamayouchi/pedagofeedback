package ma.ens.pedagofeedback.repositories;

import ma.ens.pedagofeedback.dto.Moyennes;
import ma.ens.pedagofeedback.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
            select new ma.ens.pedagofeedback.dto.Moyennes(
                avg(f.clarte), avg(f.interactivite), avg(f.utilite), count(f))
            from Feedback f
            where f.seance.enseignant.id = :enseignantId
            """)
    Moyennes moyennesParEnseignant(@Param("enseignantId") Long enseignantId);

    List<Feedback> findBySeance_Enseignant_IdAndCommentaireIsNotNullOrderByDateFeedbackDesc(Long enseignantId);
}