package mohamedjaouad.TRAINOVA.services;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.exceptions.NotFoundException;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUpdateUserDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUserDTO;
import mohamedjaouad.TRAINOVA.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Utente non trovato");
        }
        userRepository.deleteById(userId);
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