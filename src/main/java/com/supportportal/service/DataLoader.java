package com.supportportal.service;



import com.supportportal.domain.Authority;
import com.supportportal.domain.Role;
import com.supportportal.repository.AuthorityRepository;
import com.supportportal.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class DataLoader {
    @Bean
    @Transactional
    public ApplicationRunner loadData(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        return args -> {
            Date now = new Date();
            Integer defaultStatus = 1; // Example status, adjust as necessary
            Integer createdBy = 1; // Example user ID, adjust as necessary



            Authority userRead = createAuthorityIfNotExists("user:read", defaultStatus, createdBy, now, authorityRepository);
            Authority userUpdate = createAuthorityIfNotExists("user:update", defaultStatus, createdBy, now, authorityRepository);
            Authority userCreate = createAuthorityIfNotExists("user:create", defaultStatus, createdBy, now, authorityRepository);
            Authority userDelete = createAuthorityIfNotExists("user:delete", defaultStatus, createdBy, now, authorityRepository);
            Authority userImpersonate = createAuthorityIfNotExists("user:impersonate", defaultStatus, createdBy, now, authorityRepository);



            createRoleIfNotExists("user", defaultStatus, createdBy, now, Arrays.asList(userRead), roleRepository);
            createRoleIfNotExists("HR", defaultStatus, createdBy, now, Arrays.asList(userRead, userUpdate), roleRepository);
            createRoleIfNotExists("MANAGER", defaultStatus, createdBy, now, Arrays.asList(userRead, userUpdate), roleRepository);
            createRoleIfNotExists("ADMIN", defaultStatus, createdBy, now, Arrays.asList(userRead, userCreate, userUpdate), roleRepository);
            createRoleIfNotExists("SUPER_ADMIN", defaultStatus, createdBy, now, Arrays.asList(userRead, userCreate, userUpdate, userDelete, userImpersonate), roleRepository);
        };
    }



    private Authority createAuthorityIfNotExists(String name, Integer status, Integer createdBy, Date now, AuthorityRepository authorityRepository) {
        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new Authority()
                    .setName(name)
                    .setStatus(status)
                    .setCreateDate(now)
                    .setUpdateDate(now)
                    .setCreateBy(createdBy)
                    .setUpdateBy(createdBy);
            authority = authorityRepository.save(authority);
        }
        return authority;
    }



    private void createRoleIfNotExists(String name, Integer status, Integer createdBy, Date now, List<Authority> authorities, RoleRepository roleRepository) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role()
                    .setName(name)
                    .setStatus(status)
                    .setCreateDate(now)
                    .setUpdateDate(now)
                    .setCreateBy(createdBy)
                    .setUpdateBy(createdBy)
                    .setAuthorities(authorities);
            roleRepository.save(role);
        }
    }



}