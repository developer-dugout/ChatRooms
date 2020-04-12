package com.coding.pixel.chatrooms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.coding.pixel.chatrooms.Notification.Sender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallingActivity extends AppCompatActivity
{
    private TextView usernameProfile, callTxt;
    private CircleImageView callProfile;
    private ImageView cancelCallBtn, makeCallBtn;

    private String receiverUserId="", receiverUserImage="", receiverUserName="";
    private String senderUserId="", senderUserImage="", senderUserName="", currentUserID;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calling);

        mAuth = FirebaseAuth.getInstance();
        receiverUserId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usernameProfile = findViewById(R.id.call_profile_name);
        TextView marque = (TextView) findViewById(R.id.txt);
        marque.setSelected(true);
        callProfile = findViewById(R.id.call_profile_image);
        makeCallBtn = findViewById(R.id.make_call);
        cancelCallBtn = findViewById(R.id.cancel_call);

        cancelCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(CallingActivity.this, MainActivity.class);
                startActivity(chatIntent);
            }
        });
        getAndSetUserProfileInfo();
    }
    private void getAndSetUserProfileInfo()
    {
        userRef.child(receiverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if ((dataSnapshot.exists())  &&  (dataSnapshot.hasChild("image")))
                {
                    receiverUserImage = dataSnapshot.child("image").getValue().toString();
                    receiverUserName = dataSnapshot.child("name").getValue().toString();

                    Picasso.get().load(receiverUserImage).placeholder(R.drawable.profile_image).into(callProfile);
                    usernameProfile.setText(receiverUserName);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.child(receiverUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("Calling") && dataSnapshot.hasChild("Ringing"))
                {
                    final HashMap<String, Object> callingInfo = new HashMap<>();
                    callingInfo.put("uid", senderUserId);
                    callingInfo.put("name", senderUserName);
                    callingInfo.put("image", senderUserImage);
                    callingInfo.put("calling", receiverUserId);

                    userRef.child(senderUserId)
                            .child("Calling")
                            .updateChildren(callingInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        final HashMap<String, Object> ringingInfo = new HashMap<>();
                                        ringingInfo.put("uid", receiverUserId);
                                        ringingInfo.put("name", receiverUserName);
                                        ringingInfo.put("image", receiverUserImage);
                                        ringingInfo.put("calling", senderUserId);

                                        userRef.child(receiverUserId)
                                                .child("Ringing")
                                                .updateChildren(ringingInfo);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
