package com.example.dchat.cschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainChatPage extends AppCompatActivity {
    private Button singout;
    private CircleImageView userview;
    private FirebaseAuth firebaseAuth;
    private StorageReference userPhoto;
    private String username;
    private TextView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_page);
        firebaseAuth=FirebaseAuth.getInstance();
        userview=(CircleImageView)findViewById(R.id.mainuserPhoto);
        singout=(Button)findViewById(R.id.singout);
        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        userPhoto= FirebaseStorage.getInstance().getReference().child("ProfilesPhoto/"+username+".png");
        view=(TextView)findViewById(R.id.showUsrname);
        view.setText(username);
        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(userPhoto)
                .into(userview);
        singout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent=new Intent(getApplicationContext(),LongInPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
