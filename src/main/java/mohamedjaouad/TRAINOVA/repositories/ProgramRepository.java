package mohamedjaouad.TRAINOVA.repositories;

import mohamedjaouad.TRAINOVA.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProgramRepository extends JpaRepository<Program, UUID> {
    List<Program> findByUserId(UUID userId);
    List<Program> findByUserIdAndActiveTrue(UUID userId);
}