package com.hometest.service;

import com.hometest.data.Grade;
import com.hometest.data.Pupil;
import com.hometest.data.School;
import com.hometest.repository.PupilRepository;
import com.hometest.repository.SchoolRepository;
import com.hometest.utils.Collections;
import com.hometest.utils.DistanceCalcUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PupilService {

    private final PupilRepository pupilRepository;
    private final SchoolRepository schoolRepository;

    @Autowired
    public PupilService(PupilRepository pupilRepository, SchoolRepository schoolRepository) {
        this.pupilRepository = pupilRepository;
        this.schoolRepository = schoolRepository;
    }

    public long createPupil(Pupil pupil) {
        pupilRepository.save(pupil);
        return pupil.getId();
    }

    public void setFriendship(long pupilAId, long pupilBId) {
        // Retrieve both pupils from the database
        Pupil pupilA = pupilRepository.findById(pupilAId).orElseThrow(() -> new IllegalArgumentException("Pupil with ID " + pupilAId + " not found"));
        Pupil pupilB = pupilRepository.findById(pupilBId).orElseThrow(() -> new IllegalArgumentException("Pupil with ID " + pupilBId + " not found"));

        if (Collections.areFriends(pupilA, pupilB)) {
            log.info("The pupils {} and {} already friends", pupilAId, pupilBId);
            return;
        }

        // Establish friendship between them (assuming bidirectional friendship)
        pupilA.getFriends().add(pupilB);
        pupilB.getFriends().add(pupilA);

        // Save the updated pupils back to the database
        pupilRepository.save(pupilA);
        pupilRepository.save(pupilB);
    }

    public void enrollPupil(long pupilId) {
        // Retrieve the pupil from the database
        Pupil pupil = pupilRepository.findById(pupilId).orElseThrow(() -> new IllegalArgumentException("Pupil with ID " + pupilId + " not found"));

        if (pupil.getSchool() != null) {
            log.info("The pupil {} already enrolled to the school {}", pupilId, pupil.getSchool().getId());
            return;
        }

        // Find the best school for the pupil based on the business logic (e.g., maximizing the formula mentioned in your task description)
        List<School> bestSchools = findBestSchoolsForEnrollment(pupil);

        // Iterate over the best schools and enroll the pupil in the first school that meets the criteria
        for (School school : bestSchools) {
            if (canEnroll(pupil, school)) {
                enrollPupilToSchool(pupil, school);
                log.info("the pupil {} enrolled to the school {}", pupilId, school.getId());
                return; // Stop after enrolling in the first suitable school
            }
        }
        log.error("the pupil {} wasn't enrolled to any school", pupilId);
    }

    // Method to find the best schools for enrollment based on the provided formula
    private List<School> findBestSchoolsForEnrollment(Pupil pupil) {
        List<School> schools = schoolRepository.findAll();
        List<SchoolScore> schoolScores = new ArrayList<>();

        // Calculate scores for each school based on the provided formula
        for (School school : schools) {
            double distance = DistanceCalcUtils.haversine(pupil.getLat(), pupil.getLon(), school.getLat(), school.getLon());
            int numberOfFriends = calculateNumberOfFriends(pupil, school);
            double score = numberOfFriends * (1 / distance);
            schoolScores.add(new SchoolScore(school, score));
        }

        // Sort schools based on their scores (from highest to lowest)
        schoolScores.sort(Comparator.comparingDouble(SchoolScore::getScore).reversed());

        // Extract and return the sorted list of schools
        return schoolScores.stream().map(SchoolScore::getSchool).collect(Collectors.toList());
    }

    // Method to check if the pupil can be enrolled in the given school
    private boolean canEnroll(Pupil pupil, School school) {
        return calculatePupilGpa(pupil.getGrades()) >= school.getMinimumGpa() &&
                school.getEnrolledPupils().size() < school.getMaxNumberOfPupils();
    }

    private double calculatePupilGpa(List<Grade> grades) {
        if (CollectionUtils.isEmpty(grades)) {
            return 0.0; // Return 0 if no grades are available
        }

        double totalGradePoints = 0.0;

        for (Grade grade : grades) {
            totalGradePoints += grade.getGrade();
        }

        return totalGradePoints / grades.size();
    }

    // Method to calculate the number of friends a pupil has in a given school
    private int calculateNumberOfFriends(Pupil pupil, School school) {
        int numFriends = 0;

        // Iterate over the enrolled pupils of the school
        for (Pupil enrolledPupil : school.getEnrolledPupils()) {
            // Skip the current pupil since we don't count them as their own friend
            if (Objects.equals(enrolledPupil.getId(), pupil.getId())) {
                continue;
            }

            // Check if the enrolled pupil is a friend of the given pupil
            if (Collections.areFriends(pupil, enrolledPupil)) {
                numFriends++;
            }
        }

        return numFriends;
    }

    // Method to enroll the pupil to the given school
    private void enrollPupilToSchool(Pupil pupil, School school) {
        pupil.setSchool(school);
        school.getEnrolledPupils().add(pupil);

        // Save the updated pupil and school entities back to the database
        pupilRepository.save(pupil);
        schoolRepository.save(school);
    }

    @Getter
    static class SchoolScore {
        private final School school;
        private final double score;

        public SchoolScore(School school, double score) {
            this.school = school;
            this.score = score;
        }
    }
}
