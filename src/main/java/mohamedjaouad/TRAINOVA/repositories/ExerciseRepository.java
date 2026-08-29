package mohamedjaouad.TRAINOVA.repositories;

import mohamedjaouad.TRAINOVA.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {}