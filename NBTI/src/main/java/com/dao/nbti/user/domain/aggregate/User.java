package com.dao.nbti.user.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String accountId;
    private String password;
    private String name;
    private Gender gender;
    private LocalDate birthdate;
    private int point;
    private String aiText;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    private char isDeleted;
    private LocalDateTime deletedAt;
}
