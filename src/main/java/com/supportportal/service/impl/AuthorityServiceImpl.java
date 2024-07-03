package com.supportportal.service.impl;

import com.supportportal.constant.VarConstant;
import com.supportportal.domain.Authority;
import com.supportportal.domain.User;
import com.supportportal.repository.AuthorityRepository;
import com.supportportal.service.AuthorityService;
import com.supportportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Authority> getAuthorities() {
        return authorityRepository.findAllByStatusIs(VarConstant.ACTIVE);
    }

    @Override
    public void saveOrUpdate(Authority authority) {
        User userLoggedIn = userService.getUserLoggedIn();
        if(authority.getId() == null){
            authority.setCreateDate(new Date());
            authority.setStatus(VarConstant.ACTIVE);
            authority.setCreateBy(userLoggedIn.getId());

            authorityRepository.save(authority);
        }else{
            authority.setUpdateDate(new Date());
            authority.setUpdateBy(userLoggedIn.getId());
        }
        authorityRepository.save(authority);
    }

    @Override
    public void delete(Integer id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new NoResultException("Not found authority with id "+id));
        authority.setStatus(VarConstant.INACTIVE);
        authorityRepository.save(authority);
    }
}
