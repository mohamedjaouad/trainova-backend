package mohamedjaouad.TRAINOVA.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "program_exercises")
@Getter
@Setter
@NoArgsConstructor
public class ProgramExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "program_day_id", nullable = false)
    @JsonIgnore
    private ProgramDay programDay;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private String notes;
    private Integer orderIndex;
}
