package com.coding.pixel.chatrooms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewerImageActivity extends AppCompatActivity {


    private ImageView imageView;
    private String ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_image);

        imageView = findViewById(R.id.image_viewer);
        ImageUri = getIntent().getStringExtra("url");

        Picasso.get().load(ImageUri).into(imageView);
    }
}
