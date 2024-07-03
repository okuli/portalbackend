package com.supportportal.service;

import com.supportportal.domain.Role;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();

    void saveOrUpdate(Role role);

    void delete(Integer id);
}
