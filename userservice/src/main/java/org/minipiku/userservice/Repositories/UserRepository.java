package org.minipiku.userservice.Repositories;

import org.minipiku.userservice.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, String> {
    boolean existsByEmail(String email);
}