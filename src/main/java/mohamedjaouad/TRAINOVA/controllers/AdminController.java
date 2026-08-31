package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUpdateUserDTO;
import mohamedjaouad.TRAINOVA.recordsDTO.AdminUserDTO;
import mohamedjaouad.TRAINOVA.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserDTO> updateUser(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID id,
            @RequestBody AdminUpdateUserDTO body) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
        }
        return ResponseEntity.ok(adminService.updateUser(id, body));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal User currentUser,
            @PathVariable UUID id) {
        if (currentUser == null || !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accesso negato");
        }
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}