package com.mad.corti.trackap.activities.fragments;

/*
 * Trackap
 * Created by Mathieu Corti on 8/2/17.
 * mathieucorti@gmail.com
 */

// TODO: Check if this is a good practice
import com.mad.corti.trackap.activities.EditFriend;
import com.mad.corti.trackap.models.Friend;
import com.mad.corti.trackap.R;
import com.mad.corti.trackap.adapters.FriendsAdapter;
import com.mad.corti.trackap.activities.Trackap;
import com.mad.corti.trackap.helpers.ContactDataManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
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

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Friends extends Fragment {

    private static final String TAG             = "FriendsViewFragment";
    private static final int    PICK_CONTACTS   = 100;

    public  static final int    EDIT_FRIEND_REQ_CODE = 101;
    public  static final String EDIT_FRIEND_RES_CODE = "friend";

    private List<Friend>    mFriendList = new ArrayList<>();
    private RecyclerView    mRecyclerView;
    private FriendsAdapter  mAdapter;

    // ---------------------------------------------------------------------------------------------
    // PUBLIC PART ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        rootView.setTag(TAG);

        setHasOptionsMenu(true);

        addRecyclerView(rootView);
        addSwipeToDeleteFriend();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Friends
        Trackap trackap = (Trackap) getActivity();
        mFriendList = trackap.getFriendList();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_sort);
        item.setVisible(false);
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

                // Check permission for contacts.
                // TODO: Check return
                if (getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions( new String[]{Manifest.permission.READ_CONTACTS}, 0 /* TODO: Understand this param */);
                }

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, PICK_CONTACTS);
            }
        });
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_CONTACTS) {

            if (resultCode == RESULT_OK) {
                ContactDataManager contactsManager = new ContactDataManager(getActivity(), data);
                String name = "";
                String email = "";

                try {
                    name = contactsManager.getContactName();
                    email = contactsManager.getContactEmail();

                    mFriendList.add(new Friend(name, email));
                    mAdapter.notifyDataSetChanged();
                    if (getView() != null) {
                        Snackbar.make(getView(), name + getResources().getString(R.string.space_added), Snackbar.LENGTH_LONG).show();
                    }

                } catch (ContactDataManager.ContactQueryException e) {

                    // TODO: Replace by a tag.
                    Log.e("Pick contact", e.getMessage());
                }

            }
        } else if (requestCode == EDIT_FRIEND_REQ_CODE && resultCode == RESULT_OK && data != null) {

            Log.e(TAG, "onActivityResult: OK !");
            Friend outFriend = data.getParcelableExtra(EDIT_FRIEND_RES_CODE);

            for (int i = 0; i < mFriendList.size(); i++) {

                if (mFriendList.get(i).get_id().equals(outFriend.get_id())) {
                    mFriendList.set(i, outFriend);
                    return;
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.friends_recycler_view);
        mAdapter = new FriendsAdapter(mFriendList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editFriend = new Intent(getActivity(), EditFriend.class);

                int friendPos = mRecyclerView.indexOfChild(v);
                Friend friend   = mFriendList.get(friendPos);
                editFriend.putExtra("friend", friend);

                startActivityForResult(editFriend, EDIT_FRIEND_REQ_CODE);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Add swipe to remove a friend
     */
    private void addSwipeToDeleteFriend() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int pos = viewHolder.getAdapterPosition();

                Snackbar.make(getView(), mFriendList.get(pos).get_name() + getResources().getString(R.string.space_removed), Snackbar.LENGTH_LONG).show();
                mAdapter.removeItem(pos);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
