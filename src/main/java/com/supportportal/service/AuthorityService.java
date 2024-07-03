package com.supportportal.service;

import com.supportportal.domain.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> getAuthorities();

    void saveOrUpdate(Authority authority);

    void delete(Integer id);
}
