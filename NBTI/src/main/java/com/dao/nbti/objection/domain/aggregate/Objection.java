package com.dao.nbti.objection.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "objection")
public class Objection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int objectionId;

    private Integer userId;

    @NotNull
    private int problemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @NotNull
    @NotBlank
    private String reason;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    private String information;
}
