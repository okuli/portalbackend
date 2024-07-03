package com.supportportal.resource;

import com.supportportal.domain.Authority;
import com.supportportal.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = {"/","/authority"})
public class AuthorityResource {

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/list")
    public ResponseEntity<List<Authority>> getUser(){
        List<Authority> authorities = authorityService.getAuthorities();
        return new ResponseEntity<>(authorities,  OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveAuthority(@RequestBody(required = false)Authority authority){
        String message;
        try{
            authorityService.saveOrUpdate(authority);
        }catch(Exception e){
            message = "An error occurred";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteAuthority(@PathVariable("id")Integer id){
        authorityService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

}
