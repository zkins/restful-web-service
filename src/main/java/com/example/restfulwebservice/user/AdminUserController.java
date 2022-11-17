package com.example.restfulwebservice.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


@RestController
@RequestMapping("/admin")
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter
            = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate");
        
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        
        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    // GET /users/1 or users/2 -> String으로 들어오지만 파라미터를 int 로 선언하면 알아서 String->int 변환됨
    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter
            = SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "password", "ssn");
        
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);
        
        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
                
        return mapping;
    }

}

