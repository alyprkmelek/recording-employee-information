package com.melekalyaprak.myrealprojectt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerClass extends ArrayAdapter<String> {
    //firebase den hangi verileri alacağım
    private final ArrayList<String> userEmail;
    private final ArrayList<String> userName;
    private final ArrayList<String> userSurname;
    private final ArrayList<String> userAge;
    private final ArrayList<String> userPosition;
    private final ArrayList<String> userImage; //bitmap değil çünkü dosyayı değil url yi çekeceğiz
    private final Activity context;
    private ButtonListener buttonListener;


    public interface ButtonListener {
        void onClickListener(int position);
    }


    public WorkerClass(ArrayList<String> userEmail, ArrayList<String> userName, ArrayList<String> userSurname, ArrayList<String> userAge, ArrayList<String> userPosition, ArrayList<String> userImage, Activity context, ButtonListener buttonListener) {
        super(context, R.layout.custom_view, userEmail);
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userAge = userAge;
        this.userPosition = userPosition;
        this.userImage = userImage;
        this.context = context;
        this.buttonListener = buttonListener;
    }

    public  void  updateData(String item, int position){
        userEmail.remove(position);
        userName.remove(position);
        userSurname.remove(position);
        userAge.remove(position);
        userPosition.remove(position);
        userImage.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view, null, true);
        TextView userEmailText = customView.findViewById(R.id.userEmailTextViewCustomView);
        TextView userNameText = customView.findViewById(R.id.nameTextViewCustomView);
        TextView userSurnameText = customView.findViewById(R.id.surnameTextViewCustomView);
        TextView userAgeText = customView.findViewById(R.id.ageTextViewCustomView);
        TextView userPositionText = customView.findViewById(R.id.jobTextViewCustomView);
        ImageView imageView = customView.findViewById(R.id.imageViewCustomView);
        Button btn =customView.findViewById(R.id.button6);

        userEmailText.setText(userEmail.get(position));
        userNameText.setText(userName.get(position));
        userSurnameText.setText(userSurname.get(position));
        userAgeText.setText(userAge.get(position));
        userPositionText.setText(userPosition.get(position));
        Picasso.get().load(userImage.get(position)).into(imageView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               buttonListener.onClickListener(position);
            }
        });

        return customView;
    }


}
