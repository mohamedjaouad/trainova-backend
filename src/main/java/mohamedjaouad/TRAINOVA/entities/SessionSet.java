package mohamedjaouad.TRAINOVA.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "session_sets")
@Getter
@Setter
@NoArgsConstructor
public class SessionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "session_exercise_id", nullable = false)
    @JsonIgnore
    private SessionExercise sessionExercise;

    private Integer setNumber;
    private Integer reps;
    private Double weightKg;
    private boolean isPr;
}
