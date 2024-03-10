package com.hometest.utils;

import com.hometest.data.Pupil;

public class Collections {

    // Method to check if two pupils are friends
    public static boolean areFriends(Pupil pupil1, Pupil pupil2) {
        // Check if pupil1 and pupil2 have a mutual friendship relationship
        return pupil1.getFriends().contains(pupil2) && pupil2.getFriends().contains(pupil1);
    }
}
