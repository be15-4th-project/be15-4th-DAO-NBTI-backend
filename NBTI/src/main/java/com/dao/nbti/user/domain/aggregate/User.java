package com.dao.nbti.user.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthdate;
    private int point = 0;
    private String aiText=null;
    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.USER;
    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted = IsDeleted.N;
    private LocalDateTime deletedAt =null;

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.isDeleted = IsDeleted.Y;
    }

}
