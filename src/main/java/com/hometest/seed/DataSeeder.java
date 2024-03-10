package com.hometest.seed;

import com.hometest.data.Grade;
import com.hometest.data.Pupil;
import com.hometest.data.School;
import com.hometest.repository.PupilRepository;
import com.hometest.repository.SchoolRepository;
import com.hometest.utils.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Seeds initial data (not used liquibase for time saving)
 */
@Component
public class DataSeeder {

    private final PupilRepository pupilRepository;
    private final SchoolRepository schoolRepository;

    @Autowired
    public DataSeeder(PupilRepository pupilRepository, SchoolRepository schoolRepository) {
        this.pupilRepository = pupilRepository;
        this.schoolRepository = schoolRepository;
    }

    @PostConstruct
    public void seedData() {

        // Define a list of courses
        List<String> predefinedCourses = Arrays.asList("Math", "Science", "History", "Biology", "English", "Chemistry");

        // Seed Pupils
        List<Pupil> pupils = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Pupil pupil = new Pupil("Pupil " + i, getRandomLatitude(), getRandomLongitude(), generateRandomGrades(predefinedCourses));
            pupils.add(pupil);
        }
        pupilRepository.saveAll(pupils);

        // Seed Schools
        List<School> schools = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            School school = new School("School " + i, getRandomLatitude(), getRandomLongitude(), getRandomGPA(), getRandomMinNumberOfPupils());
            schools.add(school);
        }
        schoolRepository.saveAll(schools);

        // Establish friendships
        Set<Pupil> updatedPupils = new HashSet<>();
        for (Pupil pupil : pupils) {
            List<Pupil> potentialFriends = new ArrayList<>(pupils);
            potentialFriends.remove(pupil); // Exclude the pupil itself
            int numberOfFriends = (int) (Math.random() * 5); // Random number of friends (0-4)
            for (int i = 0; i < numberOfFriends; i++) {
                int randomIndex = (int) (Math.random() * potentialFriends.size());
                Pupil friend = potentialFriends.get(randomIndex);
                if (!Collections.areFriends(pupil, friend)) {
                    pupil.getFriends().add(friend);
                    friend.getFriends().add(pupil);
                }
                potentialFriends.remove(friend); // Remove friend from potential friends list
            }
            updatedPupils.add(pupil);
        }

        pupilRepository.saveAll(updatedPupils);
    }

    // Helper method to generate random grades for each pupil
    private List<Grade> generateRandomGrades(List<String> courses) {
        List<Grade> grades = new ArrayList<>();
        for (String course : courses) {
            Grade grade = new Grade();
            grade.setCourseName(course);
            grade.setGrade((int) (Math.random() * 100)); // Generating a random grade between 0 and 100
            grades.add(grade);
        }
        return grades;
    }

    // Helper method to generate random latitude
    private double getRandomLatitude() {
        return -90 + Math.random() * 180; // Range: -90 to 90
    }

    // Helper method to generate random longitude
    private double getRandomLongitude() {
        return -180 + Math.random() * 360; // Range: -180 to 180
    }

    // Helper method to generate random GPA (between 1 and 4)
    private int getRandomGPA() {
        Random random = new Random();
        return random.nextInt(4) + 1; // Range: 1 to 4
    }

    // Helper method to generate random minimum number of pupils for schools (between 10 and 50)
    private int getRandomMinNumberOfPupils() {
        Random random = new Random();
        return random.nextInt(41) + 10; // Range: 10 to 50
    }
}
