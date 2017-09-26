package com.mad.corti.trackap.activities.fragments;

/*
 * Trackap
 * Created by Mathieu Corti on 8/2/17.
 * mathieucorti@gmail.com
 */

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mad.corti.trackap.R;
import com.mad.corti.trackap.activities.EditMeeting;
import com.mad.corti.trackap.activities.Trackap;
import com.mad.corti.trackap.adapters.MeetingsAdapter;
import com.mad.corti.trackap.helpers.IsSorted;
import com.mad.corti.trackap.models.Meeting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Meetings extends Fragment {

    public  static final String EDIT_MEETING_RES_CODE   = "meeting";
    private static final int    EDIT_MEETING_REQ_CODE   = 200;
    private static final String TAG                     = "MeetingsViewFragment";

    private List<Meeting>   _meetingList = new ArrayList<>();
    private RecyclerView    _recyclerView;
    private MeetingsAdapter _adapter;


    // ---------------------------------------------------------------------------------------------
    // PUBLIC PART ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_meetings, container, false);
        rootView.setTag(TAG);

        setHasOptionsMenu(true);

        addRecyclerView(rootView);
//        addHideFabOnScroll();
        addSwipeToDeleteMeeting();

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {

            Comparator<Meeting> comp = new Comparator<Meeting>() {
                @Override
                public int compare(Meeting m1, Meeting m2) {
                    return m1.get_startDate().compareTo(m2.get_startDate());
                }
            };

            if (IsSorted.ascending(_meetingList, comp) || IsSorted.descending(_meetingList, comp)) {
                Collections.reverse(_meetingList);
            } else {
                Collections.sort(_meetingList, comp);
            }
            _adapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get Meetings
        Trackap trackap = (Trackap) getActivity();
        _meetingList = trackap.getMeetingList();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // TODO: Save and restore layout manager
        // See more at https://developer.android.com/samples/RecyclerView/src/com.example.android.recyclerview/RecyclerViewFragment.html#l154
    }

    @Override
    public void setUserVisibleHint(boolean visible) {

        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {

        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        Trackap mainActivity = (Trackap) getActivity();
        mainActivity.fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Meeting meeting     = new Meeting();
                Intent addMeeting   = new Intent(getActivity(), EditMeeting.class);

                addMeeting.putExtra("meeting", new Meeting());
                Trackap trackap = (Trackap) getActivity();
                addMeeting.putParcelableArrayListExtra("friends", trackap.getFriendList());

                startActivityForResult(addMeeting, EDIT_MEETING_REQ_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_MEETING_REQ_CODE && resultCode == RESULT_OK && data != null) {

            Meeting outMeeting = data.getParcelableExtra(EDIT_MEETING_RES_CODE);

            for (int i = 0; i < _meetingList.size(); i++) {

                if (_meetingList.get(i).get_id().equals(outMeeting.get_id())) {

                    _meetingList.set(i, outMeeting);
                    _adapter.notifyItemChanged(i);
                    return;

                } else if (i + 1 == _meetingList.size()) {

                    _adapter.addItem(outMeeting);
                }
            }

        }
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


    // ---------------------------------------------------------------------------------------------
    // PRIVATE PART --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    /**
     * Initialize the recycler view
     * @param rootView
     */
    private void addRecyclerView(View rootView) {

        _recyclerView = (RecyclerView) rootView.findViewById(R.id.meetings_recycler_view);
        _adapter = new MeetingsAdapter(_meetingList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editMeeting   = new Intent(getActivity(), EditMeeting.class);

                Meeting meeting = _meetingList.get(_recyclerView.indexOfChild(v));
                editMeeting.putExtra("meeting", meeting);
                Trackap trackap = (Trackap) getActivity();
                editMeeting.putParcelableArrayListExtra("friends", trackap.getFriendList());

                startActivityForResult(editMeeting, EDIT_MEETING_REQ_CODE);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        _recyclerView.setLayoutManager(layoutManager);
        _recyclerView.setItemAnimator(new DefaultItemAnimator());
        _recyclerView.setAdapter(_adapter);
    }

    /**
     * Hide the floating action button on scroll
     */
    private void addHideFabOnScroll() {

        final Trackap mainActivity = (Trackap) getActivity();
        _recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0) {
                    mainActivity.fab.hide();
                } else if (dy < 0) {
                    mainActivity.fab.show();
                }
            }
        });
    }

    /**
     * Add possibility to swipe to delete a meeting
     */
    private void addSwipeToDeleteMeeting() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int pos = viewHolder.getAdapterPosition();

                Snackbar.make(getActivity().findViewById(android.R.id.content), _meetingList.get(pos).get_title() +
                        getResources().getString(R.string.space_removed), Snackbar.LENGTH_LONG).show();
                _adapter.removeItem(pos);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(_recyclerView);
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


}
