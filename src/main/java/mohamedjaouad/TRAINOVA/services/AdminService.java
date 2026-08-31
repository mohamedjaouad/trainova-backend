package mohamedjaouad.TRAINOVA.services;

import mohamedjaouad.TRAINOVA.entities.Program;
import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.entities.WorkoutSession;
import mohamedjaouad.TRAINOVA.exceptions.NotFoundException;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUpdateUserDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUserDTO;
import mohamedjaouad.TRAINOVA.repositories.ProgramRepository;
import mohamedjaouad.TRAINOVA.repositories.UserRepository;
import mohamedjaouad.TRAINOVA.repositories.WorkoutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;
    private final WorkoutRepository workoutRepository;

    public AdminService(UserRepository userRepository, ProgramRepository programRepository, WorkoutRepository workoutRepository) {
        this.userRepository = userRepository;
        this.programRepository = programRepository;
        this.workoutRepository = workoutRepository;
    }

    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AdminUserDTO updateUser(UUID userId, AdminUpdateUserDTO body) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        if (body.fullName() != null) {
            user.setFullName(body.fullName());
        }
        if (body.email() != null) {
            user.setEmail(body.email());
        }
        if (body.isAdmin() != null) {
            user.setAdmin(body.isAdmin());
        }

        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));


        List<Program> programs = programRepository.findByUserId(userId);
        programRepository.deleteAll(programs);


        List<WorkoutSession> workouts = workoutRepository.findByUserId(userId);
        workoutRepository.deleteAll(workouts);


        userRepository.delete(user);
    }

    private AdminUserDTO toDTO(User user) {
        return new AdminUserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.isAdmin(),
                user.getCreatedAt()
        );
    }
}