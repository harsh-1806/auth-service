package com.harsh.auth.respositories;

import com.harsh.auth.entities.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    Optional<UserRole> findByName(String role);
}
