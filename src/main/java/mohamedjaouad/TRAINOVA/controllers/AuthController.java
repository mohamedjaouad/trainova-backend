package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.LoginPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.LoginResponseDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterResponseDTO;
import mohamedjaouad.TRAINOVA.services.AuthService;
import mohamedjaouad.TRAINOVA.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDTO register(@RequestBody @Valid RegisterPayloadDTO body) {
        return authService.registerUser(body);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody @Valid LoginPayloadDTO body) {
        String token = authService.checkCredentialsAndGenerateToken(body);
        User user = userService.findByEmail(body.email());
        return new LoginResponseDTO(token, user.isAdmin());
    }
}