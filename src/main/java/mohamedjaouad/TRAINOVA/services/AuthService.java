package mohamedjaouad.TRAINOVA.services;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.exceptions.UnauthorizedException;
import mohamedjaouad.TRAINOVA.recordsDTO.LoginPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterResponseDTO;
import mohamedjaouad.TRAINOVA.repositories.UserRepository;
import mohamedjaouad.TRAINOVA.security.JWTTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTTools jwtTools;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.password:}")
    private String adminPassword;

    public AuthService(UserService userService, UserRepository userRepository, JWTTools jwtTools, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String checkCredentialsAndGenerateToken(LoginPayloadDTO body) {
        User user = userService.findByEmail(body.email());

        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            if (adminPassword.equals(body.password()) && !user.isAdmin()) {
                user.setAdmin(true);
                userRepository.save(user);
            }
            return jwtTools.createToken(user);
        }

        throw new UnauthorizedException("Credenziali non valide");
    }

    public RegisterResponseDTO registerUser(RegisterPayloadDTO body) {
        String username = generateUniqueUsername(body.name(), body.email());

        boolean isAdmin = adminPassword.equals(body.password());

        RegisterDTO registerDTO = new RegisterDTO(username,
                body.name(),
                body.email(),
                body.password(),
                isAdmin);
        User savedUser = userService.register(registerDTO);

        return new RegisterResponseDTO(savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail());
    }

    private String generateUniqueUsername(String fullName, String email) {
        String source = (fullName != null && !fullName.isBlank())
                ? fullName
                : email.substring(0, Math.max(1, email.indexOf('@') > 0 ? email.indexOf('@') : email.length()));

        String base = source.toLowerCase().replaceAll("[^a-z0-9]", "");

        while (base.length() < 3) {
            base = base + "x";
        }
        if (base.length() > 20) {
            base = base.substring(0, 20);
        }

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            String suffixStr = String.valueOf(suffix++);
            int maxBaseLen = Math.max(1, 20 - suffixStr.length());
            String truncatedBase = base.length() > maxBaseLen ? base.substring(0, maxBaseLen) : base;
            candidate = truncatedBase + suffixStr;
        }
        return candidate;
    }
}