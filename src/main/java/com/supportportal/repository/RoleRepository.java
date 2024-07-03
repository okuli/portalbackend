package com.supportportal.repository;

import com.supportportal.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> findAllByStatusIs(Integer status);
    Optional<Role> findFirstByNameEquals(String name);
    Role findByName(String name);
}
