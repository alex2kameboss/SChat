package com.example.dchat.cschat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SingUpPage extends AppCompatActivity {
    private EditText usernameEdit, emailEdit,passwordEdit;
    private Button next;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up_page);
        usernameEdit=(EditText)findViewById(R.id.usernameEdit);
        emailEdit=(EditText)findViewById(R.id.emailEdit);
        passwordEdit=(EditText)findViewById(R.id.passwordEdit);
        next=(Button)findViewById(R.id.nextBt);
        firebaseAuth=FirebaseAuth.getInstance();
        mdatabase= FirebaseDatabase.getInstance().getReference().child("Messages");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username=usernameEdit.getText().toString();
                String email=emailEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put(username,"");
                            mdatabase.updateChildren(result);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                            Intent intent =new Intent(getApplicationContext(),UserProfilePage.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Eror",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
