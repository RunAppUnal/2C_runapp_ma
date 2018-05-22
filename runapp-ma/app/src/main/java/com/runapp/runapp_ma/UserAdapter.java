package com.runapp.runapp_ma;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

public class UserAdapter extends ArrayAdapter<User>{
    Context ctx;

    UserAdapter(Context ctx, ArrayList<User> users){
        super(ctx, R.layout.user_row,users);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rowView = inflater.inflate(R.layout.user_row, parent, false);

        final User singleUserItem = getItem(position);

        TextView name = (TextView) rowView.findViewById(R.id.d_name);
        final TextView email = (TextView) rowView.findViewById(R.id.d_email);
        ImageButton mail = (ImageButton) rowView.findViewById(R.id.b_mail);

        name.setText(singleUserItem.getName()+" "+singleUserItem.getLastname());
        email.setText(singleUserItem.getEmail());
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, singleUserItem.getEmail());
                emailIntent.setType("plain/text");
                ctx.startActivity(emailIntent);
            }
        });

        return rowView;

    }

}
