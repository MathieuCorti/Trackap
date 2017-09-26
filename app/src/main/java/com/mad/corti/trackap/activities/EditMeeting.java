package com.mad.corti.trackap.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.mad.corti.trackap.R;
import com.mad.corti.trackap.activities.fragments.Meetings;
import com.mad.corti.trackap.adapters.FriendsAdapter;
import com.mad.corti.trackap.models.Friend;
import com.mad.corti.trackap.models.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditMeeting extends AppCompatActivity {

    static final private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
    static final private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private RecyclerView        _recyclerView;
    private FriendsAdapter      _adapter;
    private ArrayList<Friend>   _meetingFriends;
    private ArrayList<Friend>   _fullFriendList;

    private Meeting meeting     = new Meeting();
    private Calendar startCal   = new GregorianCalendar();
    private Calendar endCal     = new GregorianCalendar();

    // ---------------------------------------------------------------------------------------------
    // PROTECTED PART ------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meeting);

        // Retrieve data from previous activity
        Bundle data = getIntent().getExtras();
        meeting = (Meeting) data.getParcelable("meeting");
        _fullFriendList = data.getParcelableArrayList("friends");


        // Retrieve views
        final Switch   allDay     = (Switch)   findViewById(R.id.edit_meeting_all_day);
        final EditText title      = (EditText) findViewById(R.id.edit_meeting_title);
        final TextView startDate  = (TextView) findViewById(R.id.edit_meeting_start_date);
        final TextView startTime  = (TextView) findViewById(R.id.edit_meeting_start_time);
        final TextView endDate    = (TextView) findViewById(R.id.edit_meeting_end_date);
        final TextView endTime    = (TextView) findViewById(R.id.edit_meeting_end_time);

        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Change icon
        ImageView searchIcon = (ImageView)((LinearLayout)placeAutocompleteFragment.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_location_on_24dp, getApplicationContext().getTheme()));

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                meeting.set_locationLatLng(place.getLatLng());
                meeting.set_locationName(place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                Log.e("TAG", "An error occurred: " + status);
            }
        });

        initializeFields(title, placeAutocompleteFragment, startDate, startTime, endDate, endTime);
        addRecyclerView();
        addSwipeToDeleteFriend();
        setUpAutocomplete();
        addCancelAction();
        addSaveAction(title, allDay);
        addAllDayEventListener(allDay, startTime, endTime);
        addTimePickers(startTime, endTime);
        addDatePickers(startDate, endDate);
    }


    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PRIVATE PART --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    private void setUpAutocomplete() {

        final AutoCompleteTextView addFriend = (AutoCompleteTextView) findViewById(R.id.edit_meeting_add_friends);

        ArrayAdapter<Friend> adapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_dropdown_item_1line, _fullFriendList);
        addFriend.setAdapter(adapter);
        addFriend.setThreshold(0);

        addFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Friend selected = (Friend) arg0.getAdapter().getItem(arg2);
                if (!_meetingFriends.contains(selected)) {
                    _adapter.addItem(selected);
                }
                // TODO: Display error  when friend already added.
                addFriend.setText("");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
                _recyclerView.requestFocus();
            }
        });
    }

    private void addCancelAction() {

        final ImageButton cancel = (ImageButton) findViewById(R.id.edit_meeting_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void addSaveAction(final EditText title, final Switch allDay) {

        final Button save = (Button) findViewById(R.id.edit_meeting_save);

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String titleStr    = title.getText().toString();

                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(EditMeeting.this, R.color.red_600));

                if (allDay.isChecked()){

                    startCal.set(Calendar.HOUR_OF_DAY, 0);
                    startCal.set(Calendar.MINUTE, 1);
                    endCal.set(Calendar.HOUR_OF_DAY, 23);
                    endCal.set(Calendar.MINUTE, 59);
                } else if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                    snackbar.setText("The end date and time needs to be after the start date.").show();
                    return;
                }

                if (titleStr.isEmpty()) {
                    snackbar.setText("Please enter a meeting title.").show();
                    return;
                } else if (meeting.get_locationName().isEmpty()) {
                    snackbar.setText("Please enter valid meeting location.").show();
                    return;
                }

                // Update meeting details
                meeting.set_endDate(endCal.getTime());
                meeting.set_startDate(startCal.getTime());
                meeting.set_title(titleStr);

                // Send back information
                Intent output = new Intent();
                output.putExtra(Meetings.EDIT_MEETING_RES_CODE, meeting);
                setResult(RESULT_OK, output);

                // Terminate the activity
                finish();

            }
        });
    }

    private void addSwipeToDeleteFriend() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int pos = viewHolder.getAdapterPosition();

                Snackbar.make(findViewById(android.R.id.content), meeting.get_friends().get(pos).get_name() +
                        getResources().getString(R.string.space_removed), Snackbar.LENGTH_LONG).show();
                _adapter.removeItem(pos);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(_recyclerView);
    }

    private void addRecyclerView() {

        _meetingFriends = meeting.get_friends();
        _recyclerView = (RecyclerView) findViewById(android.R.id.content).findViewById(R.id.edit_meeting_friends_recycler_view);
        _adapter = new FriendsAdapter(_meetingFriends, null);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        _recyclerView.setLayoutManager(layoutManager);
        _recyclerView.requestFocus();
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setAdapter(_adapter);
    }

    private void initializeFields(EditText title, PlaceAutocompleteFragment location, TextView startDate,
                                  TextView startTime, TextView endDate, TextView endTime) {

        startDate.setText(dateFormat.format(meeting.get_startDate()));
        endDate.setText(dateFormat.format(meeting.get_endDate()));
        startTime.setText(timeFormat.format(meeting.get_startDate()));
        endTime.setText(timeFormat.format(meeting.get_endDate()));
        title.setText(meeting.get_title());
        location.setText(meeting.get_locationName());
    }

    private void addAllDayEventListener(final Switch allDay, final TextView startTime, final TextView endTime) {

        allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                startTime.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                endTime.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void addTimePickers(final TextView startTime, final TextView endTime) {

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = startCal.get(Calendar.HOUR_OF_DAY);
                int minute = startCal.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMeeting.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                startCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startCal.set(Calendar.MINUTE, minute);
                                startTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour = endCal.get(Calendar.HOUR_OF_DAY);
                int minute = endCal.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(EditMeeting.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                endCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endCal.set(Calendar.MINUTE, hourOfDay);
                                endTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private void addDatePickers(final TextView startDate, final TextView endDate) {

        final DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                startCal.set(Calendar.YEAR, year);
                startCal.set(Calendar.MONTH, month);
                startCal.set(Calendar.DAY_OF_MONTH, day);

                startDate.setText(dateFormat.format(startCal.getTime()));
            }

        };
        final DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                endCal.set(Calendar.YEAR, year);
                endCal.set(Calendar.MONTH, month);
                endCal.set(Calendar.DAY_OF_MONTH, day);

                endDate.setText(dateFormat.format(endCal.getTime()));
            }

        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditMeeting.this, dateStart, startCal.get(Calendar.YEAR),
                        startCal.get(Calendar.MONTH),
                        startCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(EditMeeting.this, dateEnd, endCal.get(Calendar.YEAR),
                        endCal.get(Calendar.MONTH),
                        endCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
