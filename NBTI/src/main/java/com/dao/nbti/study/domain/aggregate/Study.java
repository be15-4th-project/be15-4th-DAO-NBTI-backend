package com.dao.nbti.study.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studyId;

    private Integer userId;

    private LocalDateTime createdAt;
}
