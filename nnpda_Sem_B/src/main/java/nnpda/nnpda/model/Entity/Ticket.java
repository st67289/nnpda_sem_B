package nnpda.nnpda.model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnpda.nnpda.model.enums.TicketPriority;
import nnpda.nnpda.model.enums.TicketState;
import nnpda.nnpda.model.enums.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;


@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 160)
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketState state = TicketState.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solver_id")
    private User solver;

    @NotNull
    @Column(name = "last_modified_at", nullable = false)
    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketHistory> histories = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketAttachment> attachments = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (lastModifiedAt == null) {
            lastModifiedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void onUpdate() {
        lastModifiedAt = LocalDateTime.now();
    }
}

