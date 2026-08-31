package mohamedjaouad.TRAINOVA.repositories;

import mohamedjaouad.TRAINOVA.entities.Program;
import mohamedjaouad.TRAINOVA.entities.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutSession, UUID> {


    List<WorkoutSession> findByUserIdOrderBySessionDateDesc(UUID userId);

    long countByUserId(UUID userId);


    Optional<WorkoutSession> findByIdAndUserId(UUID id, UUID userId);


    List<WorkoutSession> findByUserId(UUID userId);
}
