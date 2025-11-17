package nnpda.nnpda.model.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    @NotNull
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    @NotNull
    private User uploadedBy;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @NotBlank
    @Size(min = 1, max = 500)
    @Column(nullable = false, length = 500)
    private String url;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void onCreate() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }
}