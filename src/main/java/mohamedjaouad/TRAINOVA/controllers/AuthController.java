package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.recordsDTO.LoginPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.LoginResponseDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterPayloadDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.RegisterResponseDTO;
import mohamedjaouad.TRAINOVA.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
        return new LoginResponseDTO(token);
    }
}