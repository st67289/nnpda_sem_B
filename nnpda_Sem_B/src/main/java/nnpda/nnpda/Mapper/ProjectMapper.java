package nnpda.nnpda.Mapper;

import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.model.dto.CreateProjectDTO;
import nnpda.nnpda.model.dto.ProjectDTO;
import nnpda.nnpda.model.enums.ProjectState;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectDTO toDto(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .state(project.getState())
                .build();
    }

    public Project toEntity(CreateProjectDTO dto, User owner) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwner(owner);
        project.setState(ProjectState.ACTIVE);
        return project;
    }

}
