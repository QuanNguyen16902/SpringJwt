package com.spring.springJwt.repository;


import java.util.Optional;

import com.spring.springJwt.models.ERole;
import com.spring.springJwt.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}