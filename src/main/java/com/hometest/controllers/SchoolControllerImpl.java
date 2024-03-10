package com.hometest.controllers;

import com.hometest.data.School;
import com.hometest.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchoolControllerImpl implements SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolControllerImpl(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @Override
    @PostMapping(value = "/school")
    public long createSchool(@RequestBody School school) {
        return schoolService.createSchool(school);
    }


    @GetMapping(value = "get-all-schools")
    public List<School> getAllSchools() {
        return schoolService.getAllSchools();
    }

    @GetMapping(value = "get-school/{id}")
    public School getSchool(@PathVariable long id) {
        return schoolService.getSchool(id);
    }
}
