package com.mad.corti.trackap.activities.Data;

/*
 * Trackap
 * Created by Mathieu Corti on 9/1/17.
 * mathieucorti@gmail.com
 */

import com.google.android.gms.maps.model.LatLng;
import com.mad.corti.trackap.models.Friend;
import com.mad.corti.trackap.models.Meeting;

import java.util.ArrayList;
import java.util.Calendar;

public class FakesDataSets {

    public static ArrayList<Friend> fakeFriends() {

        ArrayList<Friend> friends = new ArrayList<>();

        friends.add(new Friend("Nicolette Schimmel" , "Christopher16@Stokes.com"    ));
        friends.add(new Friend("Allen Weissnat"     , "xLedner@gmail.com"           ));
        friends.add(new Friend("Quentin McGlynn"    , "Jadon.Sanford@Schmeler.com"  ));
        friends.add(new Friend("Ricky Bosco"        , "tVandervort@gmail.com"       ));
        friends.add(new Friend("Jonas Satterfield"  , "Darrell.Beatty@gmail.com"    ));
        friends.add(new Friend("Rachel Christian"   , "Mariana.Hudson@Brakus.com"   ));

        return friends;
    }

    public static ArrayList<Meeting> FakesMeetings() {

        ArrayList<Meeting> meetings = new ArrayList<>();

        meetings.add(new Meeting("Meeting number 1", "RMIT building 80", new LatLng(-37.80, 144.96), Calendar.getInstance().getTime(), Calendar.getInstance().getTime()));
        meetings.add(new Meeting("Meeting number 2", "Queen Victoria Market", new LatLng(-37.80, 144.95), Calendar.getInstance().getTime(), Calendar.getInstance().getTime()));

        return meetings;
    }
}
