package com.supportportal.resource;

import com.supportportal.domain.Role;
import com.supportportal.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleResource {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public ResponseEntity<List<Role>> getUser(){
        List<Role> roles = roleService.getRoles();
        return new ResponseEntity<>(roles,  OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveRole(@RequestBody(required = false)Role role){
        String message;
        try{
            roleService.saveOrUpdate(role);
        }catch(Exception e){
            message = "An error occurred";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id")Integer id){
        roleService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
