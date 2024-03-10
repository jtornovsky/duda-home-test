package com.hometest.controllers;

import com.hometest.data.Pupil;
import com.hometest.service.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PupilControllerImpl implements PupilController {

    private final PupilService pupilService;

    @Autowired
    public PupilControllerImpl(PupilService pupilService) {
        this.pupilService = pupilService;
    }

    /**
     *
     * example:
     * http://localhost:8080/pupil
         {
         "lat": 51.5074,
         "lon": 0.1278,
         "grades": [
         {
         "courseName": "Mathematics",
         "grade": 95
         },
         {
         "courseName": "Science",
         "grade": 88
         },
         {
         "courseName": "History",
         "grade": 75
         }
         ]
         }
     *
     * @param pupil
     * @return
     */
    @Override
    @PostMapping(value = "/pupil")
    public long createPupil(@RequestBody Pupil pupil) {
        return pupilService.createPupil(pupil);
    }

    /**
     *
     * example
     * http://localhost:8080/pupil/3/6
     *
     * @param pupilA
     * @param pupilB
     */
    @Override
    @PostMapping(value = "/setFriendship/{pupilA}/{pupilB}")
    public void setFriendShip(@PathVariable long pupilA, @PathVariable long pupilB) {
        pupilService.setFriendship(pupilA, pupilB);
    }

    /**
     * example:
     * http://localhost:8080/enroll/1
     *
     * @param pupilId
     */
    @Override
    @PostMapping(value = "/enroll/{pupilId}")
    public void enrollPupil(@PathVariable long pupilId) {
        pupilService.enrollPupil(pupilId);
    }
}
