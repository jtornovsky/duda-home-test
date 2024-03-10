package com.hometest.service;

import com.hometest.data.School;
import com.hometest.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    /**
     * example
     * http://localhost:8080/school
         {
         "lat": 40.7128,
         "lon": -74.0060,
         "minimumGpa": 80,
         "maxNumberOfPupils": 500,
         "grades": [
         {
         "courseName": "Mathematics",
         "grade": 90
         },
         {
         "courseName": "Science",
         "grade": 85
         },
         {
         "courseName": "History",
         "grade": 80
         }
         ]
         }

     *
     * @param school
     * @return
     */
    public long createSchool(School school) {
        schoolRepository.save(school);
        return school.getId();
    }

    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    public School getSchool(long id) {
        return schoolRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("School with ID " + id + " not found"));
    }
}
