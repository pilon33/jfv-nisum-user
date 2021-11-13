package com.nisum.test.user.repository;

import com.nisum.test.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    User findByName(String name);
    User findByEmail(String email);
}
