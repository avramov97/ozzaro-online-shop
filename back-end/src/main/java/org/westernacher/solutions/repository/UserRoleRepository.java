package org.westernacher.solutions.repository;


import org.westernacher.solutions.domain.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, String>
{
    Optional<UserRole> findByAuthority(String authority);
}
