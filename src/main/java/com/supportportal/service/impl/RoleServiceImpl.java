package com.supportportal.service.impl;

import com.supportportal.constant.VarConstant;
import com.supportportal.domain.Role;
import com.supportportal.domain.User;
import com.supportportal.repository.RoleRepository;
import com.supportportal.service.RoleService;
import com.supportportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAllByStatusIs(VarConstant.ACTIVE);
    }

    @Override
    public void saveOrUpdate(Role role) {
        User userLoggedIn = userService.getUserLoggedIn();
        if(role.getId() == null){
            role.setCreateDate(new Date());
            role.setStatus(VarConstant.ACTIVE);
            role.setCreateBy(userLoggedIn.getId());
            roleRepository.save(role);
        }else{
            role.setUpdateDate(new Date());
            role.setUpdateBy(userLoggedIn.getId());
        }
        roleRepository.save(role);
    }

    @Override
    public void delete(Integer id) {
        User userLoggedIn = userService.getUserLoggedIn();
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Not found role with id "+id));
        role.setStatus(VarConstant.INACTIVE);
        role.setUpdateDate(new Date());
        role.setUpdateBy(userLoggedIn.getId());
        roleRepository.save(role);
    }
}
