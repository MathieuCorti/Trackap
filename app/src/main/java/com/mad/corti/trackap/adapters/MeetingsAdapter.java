package com.mad.corti.trackap.adapters;

/*
 * Trackap
 * Created by Mathieu Corti on 8/7/17.
 * mathieucorti@gmail.com
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.corti.trackap.R;
import com.mad.corti.trackap.models.Meeting;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MeetingViewHolder> {

    static final private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy", Locale.ENGLISH);
    static final private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    // Attributes

    private View.OnClickListener    _clickListener;
    private List<Meeting>           _meetingList;

    // Init

    public MeetingsAdapter(List<Meeting> MeetingList, View.OnClickListener clickListener) {

        this._clickListener = clickListener;
        this._meetingList = MeetingList;
    }

    // Methods

    public void addItem(Meeting meeting) {

        _meetingList.add(meeting);
        Log.e("SORT", "addItem: before sorting");
        notifyItemInserted(_meetingList.size());
    }

    // TODO: Change position by Meeting ID
    public void removeItem(int position) {
        _meetingList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, _meetingList.size());
    }

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        private TextView title, location, startDate, endDate, startTime, endTime;

        public MeetingViewHolder(View view) {
            super(view);
            title       = (TextView) view.findViewById(R.id.itemMeeting_title);
            location    = (TextView) view.findViewById(R.id.itemMeeting_location);
            startDate   = (TextView) view.findViewById(R.id.itemMeeting_start_date);
            endDate     = (TextView) view.findViewById(R.id.itemMeeting_end_date);
            startTime   = (TextView) view.findViewById(R.id.itemMeeting_start_time);
            endTime     = (TextView) view.findViewById(R.id.itemMeeting_end_time);

            view.setOnClickListener(_clickListener);
        }
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting_list, parent, false);

        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {

        Meeting meeting     = _meetingList.get(position);
        Date    startDate   = meeting.get_startDate();
        Date    endDate     = meeting.get_endDate();

        holder.title.setText(meeting.get_title());
        holder.location.setText(meeting.get_locationName());
        holder.startDate.setText(dateFormat.format(startDate));
        holder.endDate.setText(dateFormat.format(endDate));
        holder.startTime.setText(timeFormat.format(startDate));
        holder.endTime.setText(timeFormat.format(endDate));
    }

    @Override
    public int getItemCount() {
        return _meetingList.size();
    }
}
