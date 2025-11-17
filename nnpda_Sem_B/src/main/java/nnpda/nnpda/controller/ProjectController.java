package nnpda.nnpda.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nnpda.nnpda.Mapper.ProjectMapper;
import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.CreateProjectDTO;
import nnpda.nnpda.model.dto.ProjectDTO;
import nnpda.nnpda.model.dto.ProjectUpdateDTO;
import nnpda.nnpda.service.AuthService;
import nnpda.nnpda.service.ProjectService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthService authService;
    private final ProjectMapper projectMapper;

    private User currentUser(UserDetails principal) {
        return authService.loadUserByUsername(principal.getUsername());
    }

    @Operation(
            summary = "Vypsat vlastní projekty",
            description = "Vrátí seznam projektů přihlášeného uživatele."
    )
    @GetMapping
    public List<ProjectDTO> getProjects(@AuthenticationPrincipal UserDetails principal) {
        return projectService.getProjectsForUser(currentUser(principal))
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Operation(
            summary = "Vytvořit projekt",
            description = "Vytvoří nový projekt a vrátí jeho reprezentaci. Odpověď obsahuje Location hlavičku."
    )
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid CreateProjectDTO projectDto,
                                                    @AuthenticationPrincipal UserDetails principal) {
        User me = currentUser(principal);
        Project project = projectService.createProject(projectMapper.toEntity(projectDto, me), me);
        ProjectDTO body = projectMapper.toDto(project);

        return ResponseEntity
                .created(URI.create("/projects/" + project.getId())) // 201 + Location
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(body);
    }

    @Operation(
            summary = "Získat detail projektu",
            description = "Vrátí detail projektu podle ID, pokud patří přihlášenému uživateli."
    )
    @GetMapping("/{projectId}")
    public ProjectDTO getProject(@PathVariable Long projectId,
                                 @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));
        return projectMapper.toDto(project);
    }

    @Operation(
            summary = "Nahradit projekt (PUT)",
            description = "Plně nahradí existující projekt hodnotami z těla požadavku."
    )
    @PutMapping("/{projectId}")
    public ProjectDTO updateProject(@PathVariable Long projectId,
                                    @RequestBody @Valid ProjectDTO projectDto,
                                    @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setState(projectDto.getState());
        return projectMapper.toDto(projectService.updateProject(project));
    }

    @Operation(
            summary = "Částečně upravit projekt (PATCH)",
            description = "Upraví pouze zadané vlastnosti projektu (name, description, state)."
    )
    @PatchMapping("/{projectId}")
    public ProjectDTO patchProject(@PathVariable Long projectId,
                                   @RequestBody @Valid ProjectUpdateDTO projectDto,
                                   @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));

        if (projectDto.getName() != null) project.setName(projectDto.getName());
        if (projectDto.getDescription() != null) project.setDescription(projectDto.getDescription());
        if (projectDto.getState() != null) project.setState(projectDto.getState());

        return projectMapper.toDto(projectService.updateProject(project));
    }

    @Operation(
            summary = "Smazat projekt",
            description = "Smaže projekt, pokud patří přihlášenému uživateli. Vrací 204 No Content."
    )
    @Transactional
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId,
                                              @AuthenticationPrincipal UserDetails principal) {
        Project project = projectService.getProjectByIdAndOwner(projectId, currentUser(principal));
        projectService.deleteProject(project);
        return ResponseEntity.noContent().build();
    }
}
