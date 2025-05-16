package com.dao.nbti.user.domain.repository;


import com.dao.nbti.user.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
