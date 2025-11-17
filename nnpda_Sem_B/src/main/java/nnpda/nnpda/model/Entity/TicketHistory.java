package nnpda.nnpda.model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.TicketState;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_histories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @NotNull
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_state", nullable = false)
    @NotNull
    private TicketState oldState;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_state", nullable = false)
    @NotNull
    private TicketState newState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by_id", nullable = false)
    @NotNull
    private User changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @PrePersist
    public void onCreate() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }
}