package com.example.retrovolley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, List<User> objects) { super(context, 0, objects); }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        TextView txtFullname = convertView.findViewById(R.id.txt_userFullname);
        TextView txtEmail = convertView.findViewById(R.id.txt_userEmail);
        TextView txtID = convertView.findViewById(R.id.txt_id);

        txtFullname.setText(user.getUser_fullname());
        txtEmail.setText(user.getUser_email());
        txtID.setText(String.valueOf(user.getId()));

        return convertView;
    }
}
