package ma.ens.pedagofeedback.repositories;

import ma.ens.pedagofeedback.dto.SeanceStat;
import ma.ens.pedagofeedback.entities.Seance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeanceRepository extends JpaRepository<Seance, Long> {

    List<Seance> findAllByOrderByDateSeanceDescHeureSeanceDesc();

    List<Seance> findByEnseignantIdOrderByDateSeanceDescHeureSeanceDesc(Long enseignantId);

    List<Seance> findByDateSeanceBetweenOrderByDateSeanceAscHeureSeanceAsc(LocalDate debut, LocalDate fin);

    Optional<Seance> findByJeton(String jeton);

    boolean existsByEnseignantId(Long enseignantId);

    @Query("""
            select new ma.ens.pedagofeedback.dto.SeanceStat(
                s.id, s.titre, s.matiere, s.dateSeance,
                avg(f.clarte), avg(f.interactivite), avg(f.utilite), count(f))
            from Seance s left join s.feedbacks f
            where s.enseignant.id = :enseignantId
            group by s.id, s.titre, s.matiere, s.dateSeance
            order by s.dateSeance desc
            """)
    List<SeanceStat> statistiquesParEnseignant(@Param("enseignantId") Long enseignantId);
}