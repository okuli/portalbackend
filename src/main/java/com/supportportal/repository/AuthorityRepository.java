package com.supportportal.repository;

import com.supportportal.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    List<Authority> findAllByStatusIs(Integer status);
    Authority findByName(String name);
}
