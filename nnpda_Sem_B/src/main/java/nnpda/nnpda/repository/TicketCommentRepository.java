package nnpda.nnpda.repository;

import nnpda.nnpda.model.Entity.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCommentRepository extends JpaRepository<TicketComment, Long> {
}