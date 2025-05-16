package com.dao.nbti.user.application.dto;

import com.dao.nbti.user.domain.aggregate.Gender;
import lombok.Getter;

import java.util.Date;

@Getter
public class UserCreateRequest {
    private String accountId;
    private String password;
    private String name;
    private Gender gender;
    private Date birthdate;
}
