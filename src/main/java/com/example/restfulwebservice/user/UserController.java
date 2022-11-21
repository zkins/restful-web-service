package com.example.restfulwebservice.user;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/1 or users/2 -> String으로 들어오지만 파라미터를 int 로 선언하면 알아서 String->int 변환됨
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
    // public User retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS
        EntityModel<User> model = EntityModel.of(user);

        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers()
                    );
        model.add(linkTo.withRel("all-users"));


        // return user;
        return model;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();
        
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }
}
