package com.mad.corti.trackap.models;

/*
 * Trackap
 * Created by Mathieu Corti on 8/7/17.
 * mathieucorti@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class Meeting implements Parcelable {

    // ---------------------------------------------------------------------------------------------
    // VARIABLES -----------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private String              _id = UUID.randomUUID().toString();;
    private String              _title;
    private Date                _startDate  = Calendar.getInstance().getTime();
    private Date                _endDate    = Calendar.getInstance().getTime();
    private String              _locationName;
    private LatLng              _locationLatLng = new LatLng(0, 0);
    private ArrayList<Friend>   _friends = new ArrayList<Friend>();

    // TODO: Add picture.

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // INITIALIZATION ------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public Meeting(Parcel in) {

        this._id             = in.readString();
        this._title          = in.readString();
        this._startDate      = new Date(in.readLong());
        this._endDate        = new Date(in.readLong());
        this._locationName   = in.readString();
        this._locationLatLng = in.readParcelable(LatLng.class.getClassLoader());
        in.readTypedList(_friends, Friend.CREATOR);
    }

    public Meeting() {

        _title          = "";
        _locationName   = "";
    }

    public Meeting(final String title, final String locationName, final LatLng locationLatLng,
                   final Date startDate, final Date endDate) {

        _title          = title;
        _startDate      = startDate;
        _endDate        = endDate;
        _locationName   = locationName;
        _locationLatLng = locationLatLng;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // GETTERS / SETTERS ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public String get_id() { return  _id; }

    public String get_title() {
        return _title;
    }

    public Date get_startDate() { return _startDate; }

    public Date get_endDate() { return _endDate; }

    public String get_locationName() { return _locationName; }

    public LatLng get_locationLatLng() { return _locationLatLng; }

    public ArrayList<Friend> get_friends() { return _friends; }

    public void set_title(String title) {
        this._title = title;
    }

    public void set_startDate(Date date) {
        this._startDate = date;
    }

    public void set_endDate(Date date) {
        this._endDate = date;
    }

    public void set_locationName(String locationName) { this._locationName = locationName; }

    public void set_locationLatLng(LatLng locationLatLng) { this._locationLatLng = locationLatLng; }

    public void set_friends(ArrayList<Friend> friends) { this._friends = friends; }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PARCELABLE PART -----------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Meeting createFromParcel(Parcel in) {
            return new Meeting(in);
        }

        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this._id);
        dest.writeString(this._title);
        dest.writeLong(this._startDate.getTime());
        dest.writeLong(this._endDate.getTime());
        dest.writeString(this._locationName);
        dest.writeParcelable(this._locationLatLng, flags);
        dest.writeTypedList(this._friends);
    }

    @Override
    public String toString() {

        return _title;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
