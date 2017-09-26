package com.mad.corti.trackap.adapters;

/*
 * Trackap
 * Created by Mathieu Corti on 8/2/17.
 * mathieucorti@gmail.com
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.corti.trackap.R;
import com.mad.corti.trackap.models.Friend;

import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    // Attributes

    private View.OnClickListener    _clickListener;
    private List<Friend>            _friendList;

    // Init

    public FriendsAdapter(List<Friend> friendList, View.OnClickListener clickListener) {

        this._clickListener = clickListener;
        this._friendList = friendList;
    }

    // Methods

    public void addItem(Friend friend) {
        _friendList.add(friend);
        notifyItemInserted(_friendList.size());
    }

    public void removeItem(int position) {
        _friendList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, _friendList.size());
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        public TextView name, email;

        public FriendViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);

            view.setOnClickListener(_clickListener);
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend_list, parent, false);

        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Friend friend = _friendList.get(position);
        holder.name.setText(friend.get_name());
        holder.email.setText(friend.get_email());
    }

    @Override
    public int getItemCount() {
        return _friendList.size();
    }
}