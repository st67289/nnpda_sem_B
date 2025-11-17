package nnpda.nnpda.repository;

import nnpda.nnpda.model.Entity.Project;
import nnpda.nnpda.model.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByProject(Project project);
    Optional<Ticket> findByIdAndProject(Long id, Project project);
}