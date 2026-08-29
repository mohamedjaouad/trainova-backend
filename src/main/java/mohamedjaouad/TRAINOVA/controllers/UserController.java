package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.UpdateProfileDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.UserProfileDTO;
import mohamedjaouad.TRAINOVA.services.CloudinaryService;
import mohamedjaouad.TRAINOVA.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public UserController(UserService userService, CloudinaryService cloudinaryService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/me")
    public UserProfileDTO getMyProfile(@AuthenticationPrincipal User currentUser) {
        return toProfileDTO(currentUser);
    }

    @PutMapping("/me")
    public UserProfileDTO updateMyProfile(@AuthenticationPrincipal User currentUser, @RequestBody UpdateProfileDTO body) {
        User updated = userService.updateProfile(currentUser.getId(), body);
        return toProfileDTO(updated);
    }

    @PostMapping("/me/avatar")
    public UserProfileDTO uploadAvatar(@AuthenticationPrincipal User currentUser, @RequestParam("avatar") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file);
        User updated = userService.updateAvatar(currentUser.getId(), url);
        return toProfileDTO(updated);
    }


    private UserProfileDTO toProfileDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAvatarUrl(),
                user.getDateOfBirth(),
                user.getHeightCm(),
                user.getWeightKg(),
                user.getTrainingExperienceYears(),
                user.getTrainingStyle(),
                user.getDaysPerWeek(),
                user.getLevel(),
                user.getXp()
        );
    }
}
