package mohamedjaouad.TRAINOVA.services;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.exceptions.BadRequestException;
import mohamedjaouad.TRAINOVA.exceptions.NotFoundException;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.UpdateProfileDTO;
import mohamedjaouad.TRAINOVA.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO body) {
        if (userRepository.existsByEmail(body.email())) {
            throw new BadRequestException("Esiste già un account con questa email");
        }
        if (userRepository.existsByUsername(body.username())) {
            throw new BadRequestException("Username già in uso, scegline un altro");
        }

        String hashedPassword = passwordEncoder.encode(body.password());
        User user = new User(body.username(), body.email(), hashedPassword, body.fullName());
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Nessun utente trovato con questa email"));
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public User updateProfile(UUID id, UpdateProfileDTO body) {
        User user = findById(id);
        if (body.fullName() != null) user.setFullName(body.fullName());
        if (body.dateOfBirth() != null) user.setDateOfBirth(body.dateOfBirth());
        if (body.heightCm() != null) user.setHeightCm(body.heightCm());
        if (body.weightKg() != null) user.setWeightKg(body.weightKg());
        if (body.trainingExperienceYears() != null) user.setTrainingExperienceYears(body.trainingExperienceYears());
        if (body.trainingStyle() != null) user.setTrainingStyle(body.trainingStyle());
        if (body.daysPerWeek() != null) user.setDaysPerWeek(body.daysPerWeek());
        return userRepository.save(user);
    }

    public User updateAvatar(UUID id, String avatarUrl) {
        User user = findById(id);
        user.setAvatarUrl(avatarUrl);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UUID id = UUID.fromString(username);
            return findById(id);
        } catch (IllegalArgumentException e) {
            return findByEmail(username);
        }
    }
}