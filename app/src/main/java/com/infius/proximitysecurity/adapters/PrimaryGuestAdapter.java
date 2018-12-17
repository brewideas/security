package com.infius.proximitysecurity.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.model.PrimaryGuest;
import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.custom.CircularImageView;
import com.infius.proximitysecurity.model.PrimaryGuest;

import java.util.ArrayList;

public class PrimaryGuestAdapter extends RecyclerView.Adapter<PrimaryGuestAdapter.GuestViewHolder> {

    private final Context context;
    private final ArrayList<PrimaryGuest> guestArrayList;

    public PrimaryGuestAdapter(Context context, ArrayList<PrimaryGuest> guestArrayList) {
        this.context = context;
        this.guestArrayList = guestArrayList;
    }

    @NonNull
    @Override
    public PrimaryGuestAdapter.GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primary_guest_list_item, parent, false);
        return new GuestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimaryGuestAdapter.GuestViewHolder holder, int position) {

        PrimaryGuest guest = guestArrayList.get(position);
        String name = guest.getName();
        String email = guest.getEmail();
        String mobile = guest.getMobile();
        String gender = guest.getGender();
        String picUrl = guest.getGuestPic();
        int age = guest.getAge();

        holder.guestName.setText(name + ", " + age + ", " + gender);
        holder.guestEmail.setText(email);
        holder.guestMobile.setText(mobile);
    }

    @Override
    public int getItemCount() {
        return guestArrayList.size();
    }

    public class GuestViewHolder extends RecyclerView.ViewHolder {

        CircularImageView pic;
        TextView guestName, guestEmail, guestMobile;
        public GuestViewHolder(View itemView) {
            super(itemView);
            guestName = (TextView) itemView.findViewById(R.id.guest_name);
            guestEmail = (TextView) itemView.findViewById(R.id.guest_email);
            guestMobile = (TextView) itemView.findViewById(R.id.guest_mobile);
            pic = (CircularImageView) itemView.findViewById(R.id.guest_pic);
        }
    }
}
