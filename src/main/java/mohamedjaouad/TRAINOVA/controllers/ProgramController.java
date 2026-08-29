package mohamedjaouad.TRAINOVA.controllers;

import mohamedjaouad.TRAINOVA.entities.Program;
import mohamedjaouad.TRAINOVA.entities.User;
import mohamedjaouad.TRAINOVA.recordsDTO.GenerateProgramRequest;
import mohamedjaouad.TRAINOVA.services.AIProgramGeneratorService;
import mohamedjaouad.TRAINOVA.services.ProgramGeneratorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FIX (incoerenza trovata confrontando codice e brief, sezione 4):
 * prima esisteva un solo endpoint "/generate" che chiamava sempre l'AI
 * con fallback al template. Ora sono ripristinati i due endpoint
 * documentati:
 *  - POST /programs/generate     -> generatore a template, deterministico
 *  - POST /programs/generate-ai  -> chiamata AI con fallback al template
 *
 * Il frontend (ProgramWizard.tsx) è stato aggiornato per chiamare
 * /programs/generate-ai, così l'esperienza utente (scheda generata
 * dall'AI, con fallback automatico) resta invariata.
 */
@RestController
@RequestMapping("/programs")
public class ProgramController {

    private final AIProgramGeneratorService aiProgramGeneratorService;
    private final ProgramGeneratorService programGeneratorService;

    public ProgramController(
            AIProgramGeneratorService aiProgramGeneratorService,
            ProgramGeneratorService programGeneratorService) {
        this.aiProgramGeneratorService = aiProgramGeneratorService;
        this.programGeneratorService = programGeneratorService;
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public Program generateProgramTemplate(
            @RequestBody @Valid GenerateProgramRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return programGeneratorService.generateProgram(request, currentUser);
    }

    @PostMapping("/generate-ai")
    @ResponseStatus(HttpStatus.CREATED)
    public Program generateProgramAI(
            @RequestBody @Valid GenerateProgramRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        try {
            return aiProgramGeneratorService.generateProgram(request, currentUser);
        } catch (Exception e) {
            System.err.println("Errore generazione AI, uso fallback al template: " + e.getMessage());
            return programGeneratorService.generateProgram(request, currentUser);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<Program> getCurrentProgram(@AuthenticationPrincipal User currentUser) {
        List<Program> activePrograms = programGeneratorService.getActivePrograms(currentUser.getId());
        if (activePrograms.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(activePrograms.get(0));
    }
}
