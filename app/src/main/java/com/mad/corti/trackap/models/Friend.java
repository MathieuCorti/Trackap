package com.mad.corti.trackap.models;

/*
 * Trackap
 * Created by Mathieu Corti on 8/2/17.
 * mathieucorti@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Friend implements Parcelable {

    // ---------------------------------------------------------------------------------------------
    // VARIABLES -----------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private String _id;
    private String _name;
    private String _email;
    private String _birthday;

    // TODO: Add picture.

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // INITIALIZATION ------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public Friend(Parcel in) {

        this._id    = in.readString();
        this._name  = in.readString();
        this._email = in.readString();
        this._birthday = in.readString();
    }

    public Friend() {

        _id         = UUID.randomUUID().toString();
        _name       = "";
        _email      = "";
        _birthday   = "";
    }

    public Friend(final String name, final String email) {

        _id         = UUID.randomUUID().toString();
        _name       = name;
        _email      = email;
        _birthday   = "";
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // GETTERS / SETTERS ---------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public String get_id() { return _id; }

    public String get_name() {
        return _name;
    }

    public String get_email() {
        return _email;
    }

    public String get_birthday() {
        return _birthday;
    }

    public void set_birthday(String _birthday) {
        this._birthday = _birthday;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PARCELABLE PART -----------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Friend createFromParcel(Parcel in) {
            return new Friend(in);
        }

        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this._id);
        dest.writeString(this._name);
        dest.writeString(this._email);
        dest.writeString(this._birthday);
    }

    @Override
    public String toString() {

        return _name;
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
