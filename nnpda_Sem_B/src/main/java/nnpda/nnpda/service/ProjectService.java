package nnpda.nnpda.service;

import lombok.RequiredArgsConstructor;
import nnpda.nnpda.exceptions.ForbiddenException;
import nnpda.nnpda.exceptions.NotFoundException;
import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.User;
import nnpda.nnpda.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getProjectsForUser(User user) {
        return projectRepository.findByOwner(user);
    }

    public Project createProject(Project project, User owner) {
        project.setOwner(owner);
        return projectRepository.save(project);
    }

    public Project getProjectByIdAndOwner(Long projectId, User owner) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found!"));
        if (!project.getOwner().equals(owner)) {
            throw new ForbiddenException("Access to foreign project is forbidden");
        }
        return project;
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
}

