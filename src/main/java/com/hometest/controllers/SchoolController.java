package com.hometest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hometest.data.School;

public interface SchoolController {

    @PostMapping(value = "/school")
    long createSchool(@RequestBody School school);
}
