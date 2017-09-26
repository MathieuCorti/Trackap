package com.mad.corti.trackap.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mad.corti.trackap.R;
import com.mad.corti.trackap.activities.fragments.Friends;
import com.mad.corti.trackap.models.Friend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditFriend extends AppCompatActivity {

    private Friend friend;

    // ---------------------------------------------------------------------------------------------
    // PROTECTED PART ------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);

        // Retrieve data from previous activity
        Bundle data = getIntent().getExtras();
        friend = (Friend) data.getParcelable("friend");

        // Retrieve views
        final TextView name     = (TextView) findViewById(R.id.editFriend_nameDisplay);
        final TextView email    = (TextView) findViewById(R.id.editFriend_emailDisplay);
        final TextView birthday = (TextView) findViewById(R.id.editFriend_birthday);


        addCancelAction();
        addSaveAction();
        initializeFields(name, email, birthday);
        addDatePicker(birthday);
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PRIVATE PART --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private void addCancelAction() {

        final ImageButton cancel = (ImageButton) findViewById(R.id.edit_friend_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void addSaveAction() {

        final Button save = (Button) findViewById(R.id.edit_friend_save);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Send back information
                Intent output = new Intent();
                output.putExtra(Friends.EDIT_FRIEND_RES_CODE, friend);
                setResult(RESULT_OK, output);

                // Terminate the activity
                finish();

            }
        });
    }

    private void initializeFields(TextView name, TextView email, TextView birthday) {

        name.setText(friend.get_name());
        email.setText(friend.get_email());
        birthday.setText(friend.get_birthday());
    }

    private void addDatePicker(final TextView birthday) {

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

                final String birthdayStr = dateFormat.format(calendar.getTime());
                birthday.setText(birthdayStr);
                friend.set_birthday(birthdayStr);
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditFriend.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
