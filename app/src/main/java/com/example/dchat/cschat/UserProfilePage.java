package com.example.dchat.cschat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfilePage extends AppCompatActivity {
    public Button selectPhoto, finish;
    private EditText phoneNumber;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage mdatabase;
    private CircleImageView userImage;
    private DatabaseReference databaseReference;
    public Uri uri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/schat-demo.appspot.com/o/user-3.png?alt=media&token=0f33b1d6-2f48-4549-af69-da347927d3e1");
    private int PICK_IMAGE_REQUEST = 1;
    private TextView userName;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        selectPhoto=(Button)findViewById(R.id.chosePhoto);
        finish=(Button)findViewById(R.id.Finish);
        phoneNumber=(EditText)findViewById(R.id.userPhone);
        userImage=(CircleImageView)findViewById(R.id.userPhoto);
        firebaseAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        userName=(TextView)findViewById(R.id.userName);
        userName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("VisibleForTests")
            public void onClick(View v) {
                String phone=phoneNumber.getText().toString();
                StorageReference storageReference = mdatabase.getReference().child("ProfilesPhoto/"+firebaseAuth.getCurrentUser().getDisplayName()+".png");
                UploadTask uploadTask= storageReference.putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uri=taskSnapshot.getDownloadUrl();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri).build();
                        firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                    }
                });
                HashMap<String, Object> result = new HashMap<>();
                result.put(phone, firebaseAuth.getCurrentUser().getDisplayName());
                databaseReference.updateChildren(result);
                Intent intent=new Intent(getApplicationContext(),MainChatPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
