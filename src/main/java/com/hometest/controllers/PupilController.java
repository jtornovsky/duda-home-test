package com.hometest.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hometest.data.Pupil;

public interface PupilController {

    @PostMapping(value = "/pupil")
    long createPupil(@RequestBody Pupil pupil);

    @PostMapping(value = "/setFriendship/{pupilA}/{pupilB}")
    void setFriendShip(@PathVariable long pupilA, @PathVariable long pupilB);

    @PostMapping(value = "/enroll/{pupilId}")
    void enrollPupil(@PathVariable long pupilId);
}
