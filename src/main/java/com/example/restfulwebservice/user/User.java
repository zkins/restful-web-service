package com.example.restfulwebservice.user;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @JsonIgnoreProperties(value = {"password"})
@JsonFilter("UserInfo")
public class User {
    private Integer id;

    @Size(min=2)
    private String name;

    @Past
    private Date joinDate;

    // @JsonIgnore
    private String password;
    // @JsonIgnore
    private String ssn;
}
