package nnpda.nnpda.repository;

import nnpda.nnpda.model.Entity.TicketAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketAttachmentRepository extends JpaRepository<TicketAttachment, Long> {
}