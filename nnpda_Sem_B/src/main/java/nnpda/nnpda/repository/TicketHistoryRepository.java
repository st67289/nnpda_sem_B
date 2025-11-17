package nnpda.nnpda.repository;

import nnpda.nnpda.model.Entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
}