package com.infius.proximitysecurity.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infius.proximitysecurity.R;
import com.infius.proximitysecurity.listeners.DrawerItemActionListener;
import com.infius.proximitysecurity.model.DrawerItem;

import java.util.ArrayList;


public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    ArrayList<DrawerItem> mList;
    Context mContext;
    DrawerItemActionListener listener;

    public DrawerAdapter(@NonNull Context context, int resource, ArrayList<DrawerItem> mList, DrawerItemActionListener listener) {
        super(context, resource);
        this.mList = mList;
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DrawerItem item = mList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.mItemName = convertView.findViewById(R.id.drawer_item_name);
            holder.mItemIcon = convertView.findViewById(R.id.drawer_item_icon);
            holder.layout = convertView.findViewById(R.id.drawer_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mItemIcon.setImageResource(item.getIcon());
        holder.mItemName.setText(item.getItemName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onItemClick(item);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView mItemName;
        ImageView mItemIcon;
        LinearLayout layout;
    }
}
